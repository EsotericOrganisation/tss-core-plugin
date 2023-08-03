package net.slqmy.tss_core.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class ReflectUtil {

	private static final Cache<String, Class<?>> classCache = CacheBuilder.newBuilder()
					.expireAfterWrite(5, TimeUnit.MINUTES)
					.build();

	private static final HashMap<Class<?>, Cache<String, Field>> classFieldCaches = new HashMap<>();
	private static final HashMap<Class<?>, Cache<String, Method>> classMethodCaches = new HashMap<>();

	public static @Nullable Class<?> getClassByName(String className) {
		ConcurrentMap<String, Class<?>> classMap = classCache.asMap();

		Class<?> targetClass = classMap.get(className);

		if (targetClass != null) {
			return targetClass;
		}

		try {
			targetClass = Class.forName(className);

			classMap.put(className, targetClass);
		} catch (ClassNotFoundException exception) {
			DebugUtil.handleException("Reflection Failure: Class by the name of '" + className + "' does not exist!", exception);
		}

		return null;
	}

	public static @Nullable Object getStaticFieldValue(Class<?> targetClass, String fieldName) {
		try {
			Field field = getAccessibleField(targetClass, fieldName);

			if (field != null) {
				return field.get(null);
			}

			DebugUtil.error("Reflection Failure: The static field '" + fieldName + "' of class " + targetClass.getName() + " does not exist!");
		} catch (IllegalAccessException exception) {
			DebugUtil.handleException("Reflection Failure: An unexpected exception occurred while retrieving the value of static field '" + fieldName + "' of class '" + targetClass.getName() + "'!", exception);
		}

		return null;
	}

	public static Object getStaticFieldValue(@NotNull Object object, String fieldName) {
		return getStaticFieldValue(object.getClass(), fieldName);
	}

	public static @Nullable Object getFieldValue(@NotNull Object object, String fieldName) {
		try {
			Field field = getAccessibleField(object, fieldName);

			if (field != null) {
				return field.get(object);
			}

			DebugUtil.error("Reflection Failure: Non-static field '" + fieldName + "' of object '" + object + "' does not exist! (Class: '" + object.getClass().getName() + "')");
		} catch (IllegalAccessException exception) {
			DebugUtil.handleException("Reflection Failure: An unexpected exception occurred while retrieving the value of non-static field '" + fieldName + "' of object '" + object + "'! (Class: '" + object.getClass().getName() + "')", exception);
		}

		return null;
	}

	private static @Nullable Field getAccessibleField(@NotNull Class<?> targetClass, String fieldName) {
		Cache<String, Field> classFieldCache = classFieldCaches.get(targetClass);

		if (classFieldCache == null) {
			classFieldCache = CacheBuilder.newBuilder().expireAfterWrite(
							5,
							TimeUnit.MINUTES
			).build();

			classFieldCaches.put(targetClass, classFieldCache);
		}

		Map<String, Field> classFieldMap = classFieldCache.asMap();
		Field field = classFieldMap.get(fieldName);

		if (field != null) {
			return field;
		}

		try {
			field = targetClass.getDeclaredField(fieldName);

			field.setAccessible(true);

			classFieldMap.put(fieldName, field);

			return field;
		} catch (NoSuchFieldException exception) {
			DebugUtil.handleException("Reflection Failure: An unexpected exception occurred setting access of field '" + fieldName + "' of class '" + targetClass.getName() + "' to &atrue&c!", exception);
		}

		return null;
	}

	private static @Nullable Field getAccessibleField(@NotNull Object object, String fieldName) {
		return getAccessibleField(object.getClass(), fieldName);
	}

	public static @Nullable Method getAccessibleMethod(@NotNull Class<?> targetClass, String methodName) {
		Cache<String, Method> classMethodCache = classMethodCaches.get(targetClass);

		if (classMethodCache == null) {
			classMethodCache = CacheBuilder.newBuilder().expireAfterWrite(
							5,
							TimeUnit.MINUTES
			).build();

			classMethodCaches.put(targetClass, classMethodCache);
		}

		Map<String, Method> classMethodMap = classMethodCache.asMap();
		Method method = classMethodMap.get(methodName);

		if (method != null) {
			return method;
		}

		try {
			method = targetClass.getDeclaredMethod(methodName);

			method.setAccessible(true);

			classMethodMap.put(methodName, method);

			return method;
		} catch (NoSuchMethodException exception) {
			DebugUtil.handleException("Reflection Failure: An unexpected exception occurred setting access of method '" + methodName + "' of class '" + targetClass.getName() + "' to &atrue&c!", exception);
		}

		return null;
	}

	public static @Nullable Method getAccessibleMethod(@NotNull Object object, String methodName) {
		return getAccessibleMethod(object.getClass(), methodName);
	}
}

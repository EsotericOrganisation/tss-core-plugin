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

		if (targetClass == null) {
			try {
				targetClass = Class.forName(className);
			} catch (ClassNotFoundException exception) {
				DebugUtil.handleException("Reflection Failure: Class by the name of '" + className + "' does not exist!", exception);
				return null;
			}
		} else {
			classCache.invalidate(className);
		}

		classMap.put(className, targetClass);
		return targetClass;
	}

	public static @Nullable Object getStaticFieldValue(Class<?> targetClass, String fieldName) {
		try {
			Field field = getAccessibleField(targetClass, fieldName);

			if (field != null) {
				return field.get(null);
			}

			DebugUtil.error("Reflection Failure: The static field '" + fieldName + "' of '" + targetClass.getName() + "' does not exist!");
		} catch (IllegalAccessException exception) {
			DebugUtil.handleException("Reflection Failure: An unexpected exception occurred while retrieving the value of static field '" + fieldName + "' of '" + targetClass + "'!", exception);
		}

		return null;
	}

	public static Object getStaticFieldValue(@NotNull Object object, String fieldName) {
		return getStaticFieldValue(object.getClass(), fieldName);
	}

	private static @Nullable Object getFieldValue(Class<?> targetClass, @NotNull Object object, String fieldName, Class<?> expectedFieldType) {
		try {
			Field field = getAccessibleField(targetClass, fieldName, expectedFieldType);

			if (field != null) {
				return field.get(object);
			}

			DebugUtil.error("Reflection Failure: Non-static field '" + fieldName + "' of object '" + object + "' does not exist! (" + object.getClass() + ")");
		} catch (IllegalAccessException exception) {
			DebugUtil.handleException("Reflection Failure: An unexpected exception occurred while retrieving the value of non-static field '" + fieldName + "' of object '" + object + "'! (" + object.getClass() + ")", exception);
		}

		return null;
	}

	public static @Nullable Object getFieldValue(@NotNull Object object, String fieldName, Class<?> expectedFieldType) {

		DebugUtil.log("Called getFieldValue.");
		DebugUtil.log("Object to get the value of: " + object);
		DebugUtil.log("Name of the field to get: " + fieldName);
		DebugUtil.log("Expected field type: " + expectedFieldType);

		return getFieldValue(object.getClass(), object, fieldName, expectedFieldType);
	}

	public static @Nullable Object getFieldValue(@NotNull Object object, String fieldName) {
		return getFieldValue(object.getClass(), object, fieldName, null);
	}

	private static @Nullable Field getAccessibleField(Class<?> targetClass, Class<?> originalClass, String fieldName, Class<?> expectedFieldType) {

		DebugUtil.log("Called main getAccessibleField method.");
		DebugUtil.log("targetClass: " + targetClass);
		DebugUtil.log("originalClass: " + originalClass);
		DebugUtil.log("expectedFieldType: " + expectedFieldType);

		if (targetClass == null) {
			DebugUtil.error("Reflection Failure: Couldn't find field '" + fieldName + "' as argument 'targetClass' was &9null&c!");
			DebugUtil.log("Original target class: " + originalClass);
			return null;
		}

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

		DebugUtil.log("Cached field: ", field);

		if (field == null || (expectedFieldType != null && !expectedFieldType.equals(field.getType()))) {
			try {
				field = targetClass.getDeclaredField(fieldName);

				DebugUtil.log("Found field " + field + ".");
				DebugUtil.log("Is the field type as expected?", field.getType().equals(expectedFieldType));

				if (expectedFieldType != null && !field.getType().equals(expectedFieldType)) {
					DebugUtil.log("The field type is not as expected, moving on to searching super class.");

					throw new NoSuchFieldException();
				}

				field.setAccessible(true);

				classFieldCaches.get(originalClass).put(fieldName, field);
			} catch (NoSuchFieldException exception) {
				DebugUtil.log("NoSuchFieldException created, moving on to super class.");

				field = getAccessibleField(targetClass.getSuperclass(), originalClass, fieldName, expectedFieldType);
			}
		} else {
			classFieldCache.invalidate(fieldName);
		}

		classFieldMap.put(fieldName, field);
		return field;
	}

	private static @Nullable Field getAccessibleField(Class<?> targetClass, String fieldName, Class<?> expectedFieldType) {
		return getAccessibleField(targetClass, targetClass, fieldName, expectedFieldType);
	}

	private static @Nullable Field getAccessibleField(@NotNull Object object, String fieldName, Class<?> expectedFieldType) {
		return getAccessibleField(object.getClass(), object.getClass(), fieldName, expectedFieldType);
	}

	private static @Nullable Field getAccessibleField(Class<?> targetClass, String fieldName) {
		return getAccessibleField(targetClass, targetClass, fieldName, null);
	}

	private static @Nullable Field getAccessibleField(@NotNull Object object, String fieldName) {
		return getAccessibleField(object.getClass(), object.getClass(), fieldName, null);
	}

	public static @Nullable Method getAccessibleMethod(Class<?> targetClass, Class<?> originalClass, String methodName) {
		if (targetClass == null) {
			DebugUtil.error("Reflection Failure: Couldn't find method '" + methodName + "' as argument 'targetClass' was &9null&c!");
			DebugUtil.log("Original target class: " + originalClass);
			return null;
		}

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

		if (method == null) {
			try {
				method = targetClass.getDeclaredMethod(methodName);
				method.setAccessible(true);

				classMethodCaches.get(originalClass).put(methodName, method);
			} catch (NoSuchMethodException exception) {
				method = getAccessibleMethod(targetClass.getSuperclass(), originalClass, methodName);
			}
		} else {
			classMethodCache.invalidate(methodName);
		}

		classMethodMap.put(methodName, method);
		return method;
	}

	public static @Nullable Method getAccessibleMethod(Class<?> targetClass, String methodName) {
		return getAccessibleMethod(targetClass, targetClass, methodName);
	}

	public static @Nullable Method getAccessibleMethod(@NotNull Object object, String methodName) {
		return getAccessibleMethod(object.getClass(), object.getClass(), methodName);
	}
}

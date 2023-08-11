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

  private static final Cache<String, Class<?>> classCache = CacheUtil.getNewCache();

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

  public static @Nullable Object getStaticFieldValue(Class<?> targetClass, String fieldName, boolean searchSuperClass) {
	try {
	  Field field = getAccessibleField(targetClass, fieldName, searchSuperClass);

	  if (field != null) {
		return field.get(null);
	  }

	  DebugUtil.error("Reflection Failure: The static field '" + fieldName + "' of '" + targetClass.getName() + "' does not exist!");
	} catch (IllegalAccessException exception) {
	  DebugUtil.handleException("Reflection Failure: An unexpected exception occurred while retrieving the value of static field '" + fieldName + "' of '" + targetClass + "'!", exception);
	}

	return null;
  }

  public static @Nullable Object getStaticFieldValue(Class<?> targetClass, String fieldName) {
	return getStaticFieldValue(targetClass, fieldName, true);
  }

  public static Object getStaticFieldValue(@NotNull Object object, String fieldName, boolean searchSuperClass) {
	return getStaticFieldValue(object.getClass(), fieldName, searchSuperClass);
  }

  public static Object getStaticFieldValue(@NotNull Object object, String fieldName) {
	return getStaticFieldValue(object.getClass(), fieldName, true);
  }

  private static @Nullable Object getFieldValue(Class<?> targetClass, @NotNull Object object, String fieldName, Class<?> expectedFieldType, boolean searchSuperClass) {
	try {
	  Field field = getAccessibleField(targetClass, fieldName, expectedFieldType, searchSuperClass);

	  if (field != null) {
		return field.get(object);
	  }

	  DebugUtil.error("Reflection Failure: Non-static field '" + fieldName + "' of object '" + object + "' does not exist! (" + object.getClass() + ")");
	} catch (IllegalAccessException exception) {
	  DebugUtil.handleException("Reflection Failure: An unexpected exception occurred while retrieving the value of non-static field '" + fieldName + "' of object '" + object + "'! (" + object.getClass() + ")", exception);
	}

	return null;
  }

  public static @Nullable Object getFieldValue(@NotNull Object object, String fieldName, Class<?> expectedFieldType, boolean searchSuperClass) {
	return getFieldValue(object.getClass(), object, fieldName, expectedFieldType, searchSuperClass);
  }

  public static @Nullable Object getFieldValue(@NotNull Object object, String fieldName, boolean searchSuperClass) {
	return getFieldValue(object.getClass(), object, fieldName, null, searchSuperClass);
  }

  public static @Nullable Object getFieldValue(@NotNull Object object, String fieldName, Class<?> expectedFieldType) {
	return getFieldValue(object.getClass(), object, fieldName, expectedFieldType, true);
  }

  public static @Nullable Object getFieldValue(@NotNull Object object, String fieldName) {
	return getFieldValue(object.getClass(), object, fieldName, null, true);
  }

  private static @Nullable Field getAccessibleField(Class<?> targetClass, String fieldName, Class<?> expectedFieldType, boolean searchSuperClass) {
	if (targetClass == null) {
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

	if (field == null || (expectedFieldType != null && !expectedFieldType.equals(field.getType()))) {
	  try {
		field = targetClass.getDeclaredField(fieldName);

		if (expectedFieldType != null && !field.getType().equals(expectedFieldType)) {
		  throw new NoSuchFieldException();
		}

		field.setAccessible(true);
	  } catch (NoSuchFieldException exception) {
		if (searchSuperClass) {
		  DebugUtil.log("Correct field not found, searching super class...");

		  field = getAccessibleField(targetClass.getSuperclass(), fieldName, expectedFieldType, true);
		}
	  }
	} else {
	  classFieldCache.invalidate(fieldName);
	}

	if (field != null) {
	  classFieldMap.put(fieldName, field);
	}

	return field;
  }

  private static @Nullable Field getAccessibleField(@NotNull Object object, String fieldName, Class<?> expectedFieldType, boolean searchSuperClass) {
	return getAccessibleField(object.getClass(), fieldName, expectedFieldType, searchSuperClass);
  }

  private static @Nullable Field getAccessibleField(Class<?> targetClass, String fieldName, boolean searchSuperClass) {
	return getAccessibleField(targetClass, fieldName, null, searchSuperClass);
  }

  private static @Nullable Field getAccessibleField(@NotNull Object object, String fieldName, boolean searchSuperClass) {
	return getAccessibleField(object.getClass(), fieldName, null, searchSuperClass);
  }

  public static @Nullable Method getAccessibleMethod(Class<?> targetClass, String methodName) {
	if (targetClass == null) {
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
	  } catch (NoSuchMethodException exception) {
		method = getAccessibleMethod(targetClass.getSuperclass(), methodName);
	  }
	} else {
	  classMethodCache.invalidate(methodName);
	}

	classMethodMap.put(methodName, method);
	return method;
  }

  public static @Nullable Method getAccessibleMethod(@NotNull Object object, String methodName) {
	return getAccessibleMethod(object.getClass(), methodName);
  }
}

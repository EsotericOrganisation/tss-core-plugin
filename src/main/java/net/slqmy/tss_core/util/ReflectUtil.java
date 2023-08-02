package net.slqmy.tss_core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class ReflectUtil {

	public static @Nullable Object getStaticFieldValue(Class<?> targetClass, String fieldName) {
		try {
			Field field = getAccessibleField(targetClass, fieldName);

			return field.get(null);
		} catch (IllegalAccessException exception) {
			DebugUtil.handleException(exception);
		}

		return null;
	}

	public static Object getStaticFieldValue(@NotNull Object object, String fieldName) {
		return getStaticFieldValue(object.getClass(), fieldName);
	}

	public static @Nullable Object getFieldValue(@NotNull Object object, String fieldName) {
		try {
			Field field = getAccessibleField(object, fieldName);

			return field.get(object);
		} catch (IllegalAccessException exception) {
			DebugUtil.handleException(exception);
		}

		return null;
	}

	private static @Nullable Field getAccessibleField(@NotNull Class<?> targetClass, String fieldName) {
		try {
			Field field = targetClass.getDeclaredField(fieldName);

			field.setAccessible(true);

			return field;
		} catch (NoSuchFieldException exception) {
			DebugUtil.handleException(exception);
		}

		return null;
	}

	private static Field getAccessibleField(@NotNull Object object, String fieldName) {
		return getAccessibleField(object.getClass(), fieldName);
	}
}

/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core4j;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Reflection utility class.
 */
public class DkReflections {
	/**
	 * Get class from given class name.
	 *
	 * @param className Path to class, for eg,. {@code "java.lang.String", "tool.core.reflection.DkReflections"}.
	 * @return Class which represents for given name if succeed. Otherwise return null.
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	public static <T> Class<T> getClass(String className) {
		try {
			return (Class<T>) Class.forName(className);
		}
		catch (ClassNotFoundException e) {
			return null;
		}
	}

	/**
	 * Get class of generic type of return type of given method.
	 *
	 * @return for eg, given method as {@code List<SparseArray<String>> foo()}, then
	 * return class will be {@code SparseArray<String>.class}.
	 * @see DkReflections#getLastGenericReturnClass(Method)
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	public static <T> Class<T> getGenericReturnClass(Method method) {
		Type type = method.getGenericReturnType();

		if (type instanceof ParameterizedType) {
			Type[] typeArgs = ((ParameterizedType) type).getActualTypeArguments();
			for (Type t : typeArgs) {
				System.out.println(t);
			}
			if (typeArgs.length > 0) {
				return (Class<T>) typeArgs[0];
			}
		}

		return null;
	}

	/**
	 * At subclass, this get generic-class of super class.
	 *
	 * @return For eg, returned class of class {@code HomeFragment extends BaseFragment<ViewLogic>}is {@code ViewLogic.class}.
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	public static <T> Class<T> getGenericOfSuperClass(Class subClass) {
		Type type = subClass.getGenericSuperclass();

		if (type instanceof ParameterizedType) {
			Type[] typeArgs = ((ParameterizedType) type).getActualTypeArguments();
			if (typeArgs.length > 0) {
				return (Class<T>) typeArgs[0];
			}
		}
		return null;
	}

	/**
	 * At subclass, this get generic-class of super class.
	 *
	 * @return For eg, returned class of class {@code HomeFragment extends BaseFragment<ViewLogic>}is {@code ViewLogic.class}.
	 */
	@Nullable
	public static Type[] getAllGenericOfSuperClass(Class subClass) {
		Type type = subClass.getGenericSuperclass();

		if (type instanceof ParameterizedType) {
			return ((ParameterizedType) type).getActualTypeArguments();
		}
		return null;
	}

	/**
	 * Get last class of generic type of return type of given method.
	 *
	 * @return For eg, returned class of method {@code List<SparseArray<String>> foo()} is {@code String.class}.
	 * @see DkReflections#getGenericReturnClass(Method)
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	public static <T> Class<T> getLastGenericReturnClass(Method method) {
		Type type = method.getGenericReturnType();

		if (type instanceof ParameterizedType) {
			Type[] typeArgs = ((ParameterizedType) type).getActualTypeArguments();

			while (typeArgs.length > 0) {
				type = typeArgs[0];

				if (type instanceof ParameterizedType) {
					typeArgs = ((ParameterizedType) type).getActualTypeArguments();
				}
				else {
					return (Class<T>) type;
				}
			}
		}
		return null;
	}

	/**
	 * Obtain static field value from a class.
	 *
	 * @param fieldName For eg,. frag_home
	 * @param clazz For eg,. R.layout.class
	 * @return Field value if found. Otherwise return not found value `DkConst.UID_OBJ`.
	 */
	public static Object getStaticFieldValue(String fieldName, Class<?> clazz) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			if (! field.isAccessible()) {
				field.setAccessible(true);
			}
			return field.get(null);
		}
		catch (Exception e) {
			DkLogs.error(DkReflections.class, e);
		}
		return DkConst.UID_OBJ;
	}
}

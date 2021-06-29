/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core4j.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Reflection utility class.
 */
public class DkReflections {
	/**
	 * Get class object from given class name.
	 *
	 * @param className path to class {@code "package.Class"}, like: {@code "java.lang.String", "tool.core.reflection.DkReflections"}.
	 * @return class which represents for given name.
	 */
	@SuppressWarnings("unchecked")
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
}

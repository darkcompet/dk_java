/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.database4j;

class MyOrderBy {
	private final OwnGrammar grammar;

	String type;
	String name; // column or raw sql
	String direction;

	MyOrderBy(OwnGrammar grammar, String type, String name, String direction) {
		this.grammar = grammar;
		this.type = type;
		this.name = name;
		this.direction = direction;
	}

	String compile() {
		switch (this.type) {
			case "basic":
				return this.grammar.wrapName(this.name) + ' ' + this.direction;
			case "raw":
				return this.name;
			default:
				throw new RuntimeException("Invalid type: " + this.type);
		}
	}
}

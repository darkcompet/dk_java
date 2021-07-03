/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.database4j;

/**
 * Subclass should extends this to listen trigger upsert from DAO.
 */
public abstract class DkModel {
	// Triggered before build query (before perform insert)
	public abstract void onInsert();

	// Triggered before build query (before perform update)
	public abstract void onUpdate();
}

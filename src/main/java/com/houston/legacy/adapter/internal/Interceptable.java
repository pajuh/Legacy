/**
 * @author pasi.honkanen@houston-inc.com
 */
package com.houston.legacy.adapter.internal;

import com.houston.legacy.adapter.BeforeInterception;

public interface Interceptable {

	public void setBeforeInterception(BeforeInterception beforeInterception);

	public void setInterceptions(Interceptions interceptions);

}

/**
 * @author pasi.honkanen@houston-inc.com
 */
package com.houston.legacy.adapter;

public interface Interceptable {

	public void setBeforeInterception(com.houston.legacy.adapter.BeforeInterception beforeInterception);

	public void setInterceptions(Interceptions interceptions);

}

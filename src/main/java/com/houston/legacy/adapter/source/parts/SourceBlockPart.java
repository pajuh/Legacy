/**
 * @author pasi.honkanen@houston-inc.com
 */
package com.houston.legacy.adapter.source.parts;

import java.lang.reflect.Method;
import java.util.List;


public class SourceBlockPart implements MethodPart {
	
	private final List<MethodPart> sourceBlockParts;

	public SourceBlockPart(List<MethodPart> sourceBlockParts) {
		this.sourceBlockParts = sourceBlockParts;
	}

	@Override
	public String build(Method method) {
		StringBuilder content = new StringBuilder();
		content.append("{");
		for (MethodPart part : sourceBlockParts) {
			content.append(part.build(method));
		}
		content.append("}");
		return content.toString();
	}
}
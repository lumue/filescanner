package io.github.lumue.filescanner.metadata;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tags{

public boolean contains(String s) {
 return tags.containsKey(s);
}

private final static class ExistingTag{
		private final String tag;
		private final int count;
		
		public ExistingTag(String key, int count) {
			this.tag=key;
			this.count = count;
		}
		public String getTag() {
			return tag;
		}
		
		public int getCount() {
			return count;
		}
	};
	
	private final Map<String,ExistingTag> tags=new HashMap<>();
	
	public synchronized void add(String tag){
		if(!isValid(tag))
			return;
		String key=tag.toLowerCase();
		tags.putIfAbsent(key,new ExistingTag(key,1));
		tags.computeIfPresent(key,(s, existingTag) -> new ExistingTag(key,existingTag.getCount()+1));
	}
	
	private boolean isValid(String tag) {
		if (StringUtils.isEmpty(tag)||tag.length()<=3)
			return false;
		return true;
	}
	
	public synchronized void removeTagsWithCountLowerThan(int threshhold){
		final List<ExistingTag> remaining = tags.values().stream()
				.filter(t -> t.getCount() > threshhold)
				.collect(Collectors.toList());
		tags.clear();
		remaining.forEach(r->tags.put(r.getTag(),r));
	}
	
	public Stream<String> stream(){
		return tags.keySet().stream();
	}
}

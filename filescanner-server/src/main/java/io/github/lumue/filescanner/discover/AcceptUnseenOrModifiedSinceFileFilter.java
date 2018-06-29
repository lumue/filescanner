package io.github.lumue.filescanner.discover;

import org.springframework.integration.file.filters.AbstractFileListFilter;
import org.springframework.integration.file.filters.ResettableFileListFilter;
import org.springframework.integration.file.filters.ReversibleFileListFilter;

import java.io.File;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class AcceptUnseenOrModifiedSinceFileFilter<F extends File> extends AbstractFileListFilter<F> implements ReversibleFileListFilter<F>,
		ResettableFileListFilter<F> {
	
	private static class SeenItem<F>{
		private final long timestamp;
		private final F item;
		
		private SeenItem(long timestamp, F item) {
			this.timestamp = timestamp;
			this.item = item;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof SeenItem)) return false;
			SeenItem<?> seenItem = (SeenItem<?>) o;
			return timestamp == seenItem.timestamp &&
					Objects.equals(item, seenItem.item);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(timestamp, item);
		}
	}
	
	private final Queue<SeenItem<F>> seen;
	
	private final Map<F,SeenItem<F>> seenSet = new HashMap<>();
	
	private final Object monitor = new Object();
	
	
	/**
	 * Creates an AcceptOnceFileListFilter that is based on a bounded queue. If the queue overflows,
	 * files that fall out will be passed through this filter again if passed to the
	 * {@link #filterFiles(Object[])}
	 *
	 * @param maxCapacity the maximum number of Files to maintain in the 'seen' queue.
	 */
	public AcceptUnseenOrModifiedSinceFileFilter(int maxCapacity) {
		this.seen = new LinkedBlockingQueue<>(maxCapacity);
	}
	
	/**
	 * Creates an AcceptOnceFileListFilter based on an unbounded queue.
	 */
	public AcceptUnseenOrModifiedSinceFileFilter() {
		this.seen = null;
	}
	
	
	@Override
	public boolean accept(F file) {
		synchronized (this.monitor) {
			if (this.seenSet.containsKey(file)) {
				final SeenItem<F> seenItem = this.seenSet.get(file);
				if(file.lastModified()<seenItem.timestamp)
					return false;
			}
			final SeenItem<F> seenItem = new SeenItem<>(System.currentTimeMillis(), file);
			if (this.seen != null) {
				if (!this.seen.offer(seenItem)) {
					SeenItem<F> removed = this.seen.poll();
					this.seenSet.remove(removed.item);
					this.seen.add(seenItem);
				}
			}
			this.seenSet.put(file, seenItem);
			return true;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 4.0.4
	 */
	@Override
	public void rollback(F file, List<F> files) {
		synchronized (this.monitor) {
			boolean rollingBack = false;
			for (F fileToRollback : files) {
				if (fileToRollback.equals(file)) {
					rollingBack = true;
				}
				if (rollingBack) {
					remove(fileToRollback);
				}
			}
		}
	}
	
	@Override
	public boolean remove(F fileToRemove) {
		boolean removed = this.seenSet.remove(fileToRemove)!=null;
		if (this.seen != null) {
			this.seen.remove(fileToRemove);
		}
		return removed;
	}
	
	
}

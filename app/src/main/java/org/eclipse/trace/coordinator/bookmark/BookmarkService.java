package org.eclipse.trace.coordinator.bookmark;

import java.util.List;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.bookmark.Bookmark;
import org.jvnet.hk2.annotations.Service;

@Service
public class BookmarkService {
	public List<Bookmark> getBookmarks(final TraceServer traceServer, final UUID experimentUuid) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	public Bookmark getBookmark(final TraceServer traceServer, final UUID experimentUuid, final UUID bookmarkUuid) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	public Bookmark createBookmark(final TraceServer traceServer, final UUID experimentUuid, final Bookmark bookmark) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	public Bookmark updateBookmark(final TraceServer traceServer, final UUID experimentUuid, final UUID bookmarkUuid,
			Bookmark bookmark) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	public Bookmark deleteBookmark(final TraceServer traceServer, final UUID experimentUuid, final UUID bookmarkUuid) {
		throw new UnsupportedOperationException("Not Implemented");
	}
}

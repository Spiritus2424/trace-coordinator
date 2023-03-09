package org.eclipse.trace.coordinator.bookmark;

import java.util.List;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.models.bookmark.Bookmark;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BookmarkService {
    public List<Bookmark> getBookmarks(TraceServer traceServer, UUID experimentUuid) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public Bookmark getBookmark(TraceServer traceServer, UUID experimentUuid, UUID bookmarkUuid) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public Bookmark createBookmark(TraceServer traceServer, UUID experimentUuid, Bookmark bookmark) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public Bookmark updateBookmark(TraceServer traceServer, UUID experimentUuid, UUID bookmarkUuid, Bookmark bookmark) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public Bookmark deleteBookmark(TraceServer traceServer, UUID experimentUuid, UUID bookmarkUuid) {
        throw new UnsupportedOperationException("Not Implemented");
    }
}

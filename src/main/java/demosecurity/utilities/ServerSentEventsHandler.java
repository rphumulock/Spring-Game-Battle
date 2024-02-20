//package demosecurity.utilities;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.function.Consumer;
//
//public class ServerSentEventsHandler {
//
//    private HttpServletResponse response;
//    private boolean usingCompression;
//    private int compressionMinBytes;
//    private boolean shouldLogPanics;
//    private boolean hasPanicked;
//
//    // Private constructor to force the use of the factory method
//    private ServerSentEventsHandler(HttpServletResponse response, boolean usingCompression, int compressionMinBytes, boolean shouldLogPanics) {
//        this.response = response;
//        this.usingCompression = usingCompression;
//        this.compressionMinBytes = compressionMinBytes;
//        this.shouldLogPanics = shouldLogPanics;
//        this.hasPanicked = false;
//    }
//
//    // Static factory method
//    public static ServerSentEventsHandler NewSSE(HttpServletResponse response, HttpServletRequest request) {
//        // Set response headers
//        response.setHeader("Cache-Control", "no-cache");
//        response.setHeader("Connection", "keep-alive");
//        response.setHeader("Content-Type", "text/event-stream");
//
//        // Attempt to flush the response to ensure the client receives headers immediately
//        try {
//            response.flushBuffer();
//        } catch (Exception e) {
//            // Handle exception or panic based on your application needs
//            throw new RuntimeException("Response writer does not support flushing", e);
//        }
//
//        // Check if the client accepts compression
//        boolean usingCompression = request.getHeader("Accept-Encoding") != null && request.getHeader("Accept-Encoding").contains("gzip");
//
//        // Return a new instance
//        return new ServerSentEventsHandler(response, usingCompression, 256, true);
//    }
//
//    public void sendMultiData(List<String> dataArr, Consumer<SSEEvent>... opts) {
//        if (hasPanicked && !dataArr.isEmpty()) {
//            return;
//        }
//
//        try {
//            SSEEvent evt = new SSEEvent();
//
//
//            for (Consumer<SSEEvent> opt : opts) {
//                opt.accept(evt);
//            }
//
//            int totalSize = 0;
//
//            if (!evt.getEvent().isEmpty()) {
//                String evtFmt = "event: " + evt.getEvent() + "\n";
//                totalSize += writeWithCheck(evtFmt);
//            }
//            if (!evt.getId().isEmpty()) {
//                String idFmt = "id: " + evt.getId() + "\n";
//                totalSize += writeWithCheck(idFmt);
//            }
//            if (evt.getRetry() > 0) {
//                String retryFmt = "retry: " + evt.getRetry() + "\n";
//                totalSize += writeWithCheck(retryFmt);
//            }
//
//            int lastDataIdx = evt.getData().size() - 1;
//            for (int i = 0; i < evt.getData().size(); i++) {
//                String d = evt.getData().get(i);
//                String dataFmt = "data: " + d;
//                totalSize += writeWithCheck(dataFmt);
//
//                if (i != lastDataIdx || !evt.isSkipMinBytesCheck()) {
//                    newlinePadding(totalSize, 3); // Assuming newlinePadding handles compression logic
//                }
//                writer.println(); // Writing newline
//            }
//            writer.println(); // Writing double newline to end the event
//
//            flusher.flush(); // Assuming flusher.flush() is equivalent to Go's flusher.Flush()
//        } catch (Exception e) {
//            if (shouldLogPanics) {
//                hasPanicked = true;
//                logPrintf("recovered from panic: %v", e); // Assuming logPrintf is a logging method
//            }
//        }
//    }
//
//    private int writeWithCheck(String content) throws IOException {
//        writer.print(content);
//        return content.getBytes().length;
//    }
//
//    private void newlinePadding(int totalSize, int newlineSuffixCount) throws IOException {
//        if (usingCompression && totalSize + newlineSuffixCount < compressionMinBytes) {
//            int bufSize = compressionMinBytes - totalSize - newlineSuffixCount;
//            String padding = String.format("%" + bufSize + "s", "");
//            writer.print(padding);
//        }
//    }
//
//
//}

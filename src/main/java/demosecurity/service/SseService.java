package demosecurity.service;

import demosecurity.utilities.FragmentMergeType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter createEmitter() {
//        return new SseEmitter(Long.MAX_VALUE); // Long-lived emitter
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        return emitter;
    }

    public void sendToAll(Object data) {
        List<SseEmitter> failedEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(data);
            } catch (IOException e) {
                emitter.completeWithError(e);
                failedEmitters.add(emitter);
            }
        });
        emitters.removeAll(failedEmitters);
    }

    public void sendFragment(SseEmitter emitter, String selector, FragmentMergeType mergeType, long settleDuration, String fragmentHtml) throws IOException {
        long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE; // Simple way to generate a unique ID
        StringBuilder dataBuilder = new StringBuilder();
        dataBuilder.append("selector ").append(selector).append("\n");
        dataBuilder.append("merge ").append(mergeType.toString().toLowerCase()).append("\n");
        dataBuilder.append("settle ").append(settleDuration).append("\n");
        dataBuilder.append("fragment ").append(fragmentHtml);

        // Preparing the custom event format
        SseEmitter.SseEventBuilder event = SseEmitter.event()
                .id(String.valueOf(id))
                .name("datastar-fragment") // Your custom event name
                .data(dataBuilder.toString());

        emitter.send(event);
    }

//    public void RenderFragment(ServerSentEventsHandler sse, String template, RenderFragmentOptions ops) { {
//        RenderFragmentOptions  options = new RenderFragmentOptions(
//            FragmentSelectorUseID,
//            FragmentMergeMorphElement,
//0
//        );
//
//        List<String> dataRows = new ArrayList<>();
//        dataRows.add(String.format("selector %s", options.getQuerySelector()));
//        dataRows.add(String.format("merge %s", options.getMerge()));
//        // Assuming settleDuration is a Duration object in Java, we get milliseconds from it
//        dataRows.add(String.format("settle %d", options.getSettleDuration()));
//        dataRows.add(String.format("fragment %s", template));
//
//
//
//        sse.sendMultiData(dataRows, );
//    }
//
//    public void sendFragment(SseEmitter emitter, RenderFragmentOptions options, String htmlContent) throws IOException {
//        long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE; // Simple way to generate a unique ID
//        StringBuilder dataBuilder = new StringBuilder();
//        dataBuilder.append(" selector ").append(options.getQuerySelector()).append("\n");
//        dataBuilder.append(" merge ").append(options.getMerge().toString().toLowerCase()).append("\n");
//        dataBuilder.append(" settle ").append(options.getSettleDuration()).append("\n");
//        dataBuilder.append(" fragment ").append(htmlContent);
//
//        // Preparing the custom event format
//        SseEmitter.SseEventBuilder event = SseEmitter.event()
//                .name("datastar-fragment") // Your custom event name
//                .id(String.valueOf(id))
//                .data(dataBuilder.toString());
//
//        emitter.send(event);
//    }
}



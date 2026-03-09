package com.valhala.optimacvspring.analysis.service;

import com.valhala.optimacvspring.resume.events.CvUploadedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CvAnalysisService {
 
    
    private final ChatClient chatClient;

    public CvAnalysisService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @ApplicationModuleListener
    public void onCvUploaded(CvUploadedEvent event) {
        log.info("Received event for Resume ID: {}", event.resumeId());

        try {
            String analysisResult = analyzeCv(event.extractedText());

            log.info("AI Analysis Result: \n{}", analysisResult);
        } catch (Exception e) {
            log.error("Failed to analyze CV for Resume ID: {}", event.resumeId(), e);
        }
    }


    public String analyzeCv(String cvText) {
        String systemPrompt = """
            أنت خبير توظيف (HR) ومبرمج محترف. 
            مهمتك هي تحليل السيرة الذاتية (CV) التالية التي سيمدك بها المستخدم.
            قم بإعطاء:
            1. تقييم عام للسيرة الذاتية (من 10).
            2. نقاط القوة.
            3. نقاط الضعف أو الأشياء المفقودة.
            4. نصائح عملية لتحسين الـ CV ليتناسب مع أنظمة الـ ATS.
            أجب بلغة احترافية وواضحة.
            """;

        return chatClient.prompt()
                .system(systemPrompt)
                .user(cvText)
                .call()
                .content();
    }
}

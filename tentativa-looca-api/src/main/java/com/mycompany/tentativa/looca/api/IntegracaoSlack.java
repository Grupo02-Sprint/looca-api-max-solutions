package com.mycompany.tentativa.looca.api;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.WebhookResponse;

/**
 *
 * @author daniel
 */
public class IntegracaoSlack {
//    private static String webHooksUrl = "https://hooks.slack.com/services/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";
//    private static String slackChannel = "alertas";
//
//    public static void receberMensagem(Double metrica, Double ideal, String componente){
//        
//        // Aqui precisa colocar a frase com base na validação
//        // EX: se for memoria RAM, envia uma mensagem dizendo que a memoria ram
//        // tá em metrica uso
//        String msg = String.format("%.2f", metrica);
//        enviarMensagemParaSlack(msg);
//    }
//    
//    public static void enviarMensagemParaSlack(String mensagem) {
//        try {
//            StringBuilder msgBuilder = new StringBuilder();
//            msgBuilder.append(mensagem);
//
//            Payload payload = Payload.builder().channel(slackChannel).text(msgBuilder.toString()).build();
//
//            WebhookResponse wbResp = Slack.getInstance().send(webHooksUrl, payload);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

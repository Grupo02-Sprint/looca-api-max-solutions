package com.mycompany.tentativa.looca.api;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.WebhookResponse;
import com.mycompany.tentativa.looca.api.conexao.TokenDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author daniel
 */
public class IntegracaoSlack {

    private static String webHooksUrl = "https://hooks.slack.com/services/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";
    private static String slackChannel = "#alertas";
    private static String slackToken;

    private void defineToken() {
        try {
            TokenDAO token = new TokenDAO();
            slackToken = token.getToken();
        } catch (SQLException ex) {
            Logger.getLogger(IntegracaoSlack.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void receberMensagem(Double metrica, Integer fkMaquina, String componente) {

        // Aqui precisa colocar a frase com base na validação
        // EX: se for memoria RAM, envia uma mensagem dizendo que a memoria ram
        // tá em metrica uso
        String msg = String.format("Alerta no componente %s da maquina %d, %.2f de uso",componente,fkMaquina, metrica);
        enviarMensagemParaSlack(msg);
    }

    public  void enviarMensagemParaSlack(String mensagem) {
        try {
            StringBuilder msgBuilder = new StringBuilder();
            msgBuilder.append(mensagem);

            Payload payload = Payload.builder().channel(slackChannel).text(msgBuilder.toString()).build();

            WebhookResponse wbResp = Slack.getInstance().send(webHooksUrl, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

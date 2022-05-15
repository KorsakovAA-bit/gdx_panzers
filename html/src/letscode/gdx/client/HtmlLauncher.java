package letscode.gdx.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import letscode.gdx.Starter;
import letscode.gdx.client.ws.EventListenerCallback;
import letscode.gdx.client.ws.WebSocket;

import java.util.concurrent.atomic.AtomicBoolean;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                // Resizable application, uses available space in browser
                return new GwtApplicationConfiguration(true);
                // Fixed size application:
                //return new GwtApplicationConfiguration(480, 320);
        }

        private native WebSocket getWebSocket(String url)
                /*-{
                        return new WebSocket(url);
                }-*/
        ;

        private native void log(Object obj)
                /*-{
                        console.log(obj);
                }-*/
        ;

        @Override
        public ApplicationListener createApplicationListener () {
                WebSocket client = getWebSocket("ws://192.168.1.6:9000/ws");
                AtomicBoolean once = new AtomicBoolean(false);

                Starter starter = new Starter();
                EventListenerCallback callback = event -> {
                        if (!once.get()) {
                                client.send("hello");
                                once.set(true);
                        }
                        log(event.getData());
                };
                client.addEventListener("open", callback);
                client.addEventListener("close", callback);
                client.addEventListener("error", callback);
                client.addEventListener("message", callback);

                return starter;
        }
}
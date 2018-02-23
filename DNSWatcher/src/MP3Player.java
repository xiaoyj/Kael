import java.io.BufferedInputStream;
import java.net.URL;

import javazoom.jl.player.Player;
import javax.sound.sampled.*;

/**
 * Created by Administrator on 2016/12/23 
 * */
public class MP3Player {
    public Clip clip;

    public static Player player;

    public MP3Player(URL musicRouting) {
        try {
            BufferedInputStream buffer = new BufferedInputStream(musicRouting.openStream());
            player = new Player(buffer);
            player.play();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
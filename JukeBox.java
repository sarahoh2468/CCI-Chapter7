import java.util.Queue;

public class JukeBox {
    // Design a musical jukebox using object-oriented
    public class CD {
        /* data for id, artist, songs, etc */
    }

    public class Song {
        /* data for id, CD, title, length, etc */
    }

    public class Playlist {
        private Song song;
        private Queue<Song> queue;
        public PlayList(Song song, Queue<Song> queue) {
            //
        }
        public Song getNextSToPlay() {
            return queue.peek();
        }
        public void queueUpSong(Song s) {
            queue.add(s);
        }
    }

    public class CDPlayer {
        private Playlist p;
        private CD c;

        public CDPlayer(CD c, Playlist p) {

        }
        public CDPlayer(Playlist p) {
            this.p = p;
        }
        public CDPlayer(CD c) {
            this.c = c;
        }

        public void playSong(Song s) {
            //
        }

        public Playlist getPlaylist() {
            return p;
        }

        public void setPLaylist(Playlist p) {
            this.p = p;
        }

        public CD getCD() {
            return c;
        }
        public void setCD(CD c) {
            this.c = c;
        }
    }

}

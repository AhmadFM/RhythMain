/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.model;

/**
 *
 * @author LENOVO
 */
public class Song {
    private int songId;
    private String title;
    private String artist;
    private String creator;
    private double bpm;
    private String audioPath;

    public Song(
            int songId,
            String title,
            String artist,
            String creator,
            double bpm,
            String audioPath) {

        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.creator = creator;
        this.bpm = bpm;
        this.audioPath = audioPath;
    }
    public int getSongId() {
    return songId;
}

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getCreator() {
        return creator;
    }

    public double getBpm() {
        return bpm;
    }

    public String getAudioPath() {
        return audioPath;
    }

      
}

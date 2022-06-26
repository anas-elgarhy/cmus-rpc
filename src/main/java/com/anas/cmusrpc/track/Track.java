package com.anas.cmusrpc.track;

import com.anas.cmusrpc.util.CMUSOutParserUtil;

import java.util.EnumMap;
import java.util.Map;

public class Track {
    private TrackInfo trackInfo;
    private Map<Tag, String> tags;

    public Track() {
        tags = new EnumMap<>(Tag.class);
        trackInfo = null;
    }

    public TrackInfo getTrackInfo() {
        return trackInfo;
    }

    public Map<Tag, String> getTags() {
        return tags;
    }

    protected void setTrackInfo(TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
    }

    protected void setTags(Map<Tag, String> tags) {
        this.tags = tags;
    }

    public String getTag(Tag key) {
        if (tags.containsKey(key)) {
            return tags.get(key);
        }
        return "Unknown";
    }

    public void update(Track track) {
        if ((this.trackInfo != null && track.getTrackInfo() != null && track.getTrackInfo().getFile() != null) &&
                track.getTrackInfo().getFile().equals(this.trackInfo.getFile())) {
            // Update the current time and the status if the file is the same
            this.trackInfo.setCurrentTime(track.getTrackInfo().getCurrentTime());
            this.trackInfo.setStatus(track.getTrackInfo().getStatus());
        } else {
            // Update the all attributes if the file is different
            this.trackInfo = track.getTrackInfo();
            this.tags = track.getTags();
        }
    }

    public static Track build(String response) {
        final var track = new Track();

        track.setTags(CMUSOutParserUtil.parse(response));
        track.setTrackInfo(new TrackInfo(response));

        return track;
    }

    /**
     * If the track has a title, return it. Otherwise, return the name of the file
     *
     * @return The name of the track.
     */
    public String getTrackName() {
        var name = getTag(Tag.TITLE);
        if (name.equals("Unknown")) {
            // Getting the name of the file and removing the extension.
            name = trackInfo.getFile().getName()
                    .substring(0, trackInfo.getFile().getName().lastIndexOf("."));
        }
        return name;
    }
}
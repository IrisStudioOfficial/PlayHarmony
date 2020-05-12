package iris.playharmony.model;

public enum SongPlayMode {

    SEQUENTIAL,
    RANDOM,
    SELF;

    public static SongPlayMode getDefault() {
        return SongPlayMode.values()[0];
    }
}

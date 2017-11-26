var $ = require('jquery');
const musicScript = require('./musicScript');

// Test data
var spotifySong = JSON.parse('{"album":{"album_type":"album","artists":[{"external_urls":{"spotify":"https://open.spotify.com/artist/6x2LnllRG5uGarZMsD4iO8"},"href":"https://api.spotify.com/v1/artists/6x2LnllRG5uGarZMsD4iO8","id":"6x2LnllRG5uGarZMsD4iO8","name":"Thomas Rhett","type":"artist","uri":"spotify:artist:6x2LnllRG5uGarZMsD4iO8"}],"available_markets":["AD","AR","AT","AU","BE","BG","BO","BR","CA","CH","CL","CO","CR","CY","CZ","DE","DK","DO","EC","EE","ES","FI","FR","GB","GR","GT","HK","HN","HU","ID","IE","IS","IT","JP","LI","LT","LU","LV","MC","MT","MX","MY","NI","NL","NO","NZ","PA","PE","PH","PL","PT","PY","SE","SG","SK","SV","TH","TR","TW","US","UY","VN"],"external_urls":{"spotify":"https://open.spotify.com/album/4ykbJPI8MPscFzROE8dqLy"},"href":"https://api.spotify.com/v1/albums/4ykbJPI8MPscFzROE8dqLy","id":"4ykbJPI8MPscFzROE8dqLy","images":[{"height":640,"url":"https://i.scdn.co/image/04ff24682bc24c49708307586bc30b3431aa6988","width":640},{"height":300,"url":"https://i.scdn.co/image/b19cc380033d32379b88e4fef1d0a8db4db73679","width":300},{"height":64,"url":"https://i.scdn.co/image/5421f8a730a006a60ce5c562e76462b43fd00984","width":64}],"name":"Tangled Up","type":"album","uri":"spotify:album:4ykbJPI8MPscFzROE8dqLy"},"artists":[{"external_urls":{"spotify":"https://open.spotify.com/artist/6x2LnllRG5uGarZMsD4iO8"},"href":"https://api.spotify.com/v1/artists/6x2LnllRG5uGarZMsD4iO8","id":"6x2LnllRG5uGarZMsD4iO8","name":"Thomas Rhett","type":"artist","uri":"spotify:artist:6x2LnllRG5uGarZMsD4iO8"}],"available_markets":["AD","AR","AT","AU","BE","BG","BO","BR","CA","CH","CL","CO","CR","CY","CZ","DE","DK","DO","EC","EE","ES","FI","FR","GB","GR","GT","HK","HN","HU","ID","IE","IS","IT","JP","LI","LT","LU","LV","MC","MT","MX","MY","NI","NL","NO","NZ","PA","PE","PH","PL","PT","PY","SE","SG","SK","SV","TH","TR","TW","US","UY","VN"],"disc_number":1,"duration_ms":227693,"explicit":false,"external_ids":{"isrc":"USLXJ1507315"},"external_urls":{"spotify":"https://open.spotify.com/track/1zWZvrk13cL8Sl3VLeG57F"},"href":"https://api.spotify.com/v1/tracks/1zWZvrk13cL8Sl3VLeG57F","id":"1zWZvrk13cL8Sl3VLeG57F","name":"T-Shirt","popularity":72,"preview_url":null,"track_number":7,"type":"track","uri":"spotify:track:1zWZvrk13cL8Sl3VLeG57F"}');
var jamendoSong = JSON.parse('{"id":"1452541","name":"On Everything","duration":239,"artist_id":"498582","artist_name":"Axel Antunes","artist_idstr":"Axel_Antunes","album_name":"#1","album_id":"168787","license_ccurl":"http://creativecommons.org/licenses/by-sa/3.0/","position":1,"releasedate":"2017-05-29","album_image":"https://imgjam2.jamendo.com/albums/s168/168787/covers/1.200.jpg","audio":"https://mp3l.jamendo.com/?trackid=1452541&format=mp31&from=app-d328628b","audiodownload":"https://mp3d.jamendo.com/download/track/1452541/mp32/","prourl":"","shorturl":"http://jamen.do/t/1452541","shareurl":"http://www.jamendo.com/track/1452541","image":"https://imgjam2.jamendo.com/albums/s168/168787/covers/1.200.jpg"}');
var deezerSong = JSON.parse('{"id":118655608,"readable":true,"title":"Unspeakable World","title_short":"Unspeakable World","title_version":"","link":"https://www.deezer.com/track/118655608","duration":283,"rank":305535,"explicit_lyrics":false,"preview":"https://e-cdns-preview-9.dzcdn.net/stream/992022ec4f11760ff134dd41c61c9e99-2.mp3","artist":{"id":4052492,"name":"GoGo Penguin","link":"https://www.deezer.com/artist/4052492","picture":"https://api.deezer.com/artist/4052492/image","picture_small":"https://e-cdns-images.dzcdn.net/images/artist/93e964a5b63342ebf7ea6a59885e50be/56x56-000000-80-0-0.jpg","picture_medium":"https://e-cdns-images.dzcdn.net/images/artist/93e964a5b63342ebf7ea6a59885e50be/250x250-000000-80-0-0.jpg","picture_big":"https://e-cdns-images.dzcdn.net/images/artist/93e964a5b63342ebf7ea6a59885e50be/500x500-000000-80-0-0.jpg","picture_xl":"https://e-cdns-images.dzcdn.net/images/artist/93e964a5b63342ebf7ea6a59885e50be/1000x1000-000000-80-0-0.jpg","tracklist":"https://api.deezer.com/artist/4052492/top?limit=50","type":"artist"},"album":{"id":12337850,"title":"Man Made Object (Deluxe)","cover":"https://api.deezer.com/album/12337850/image","cover_small":"https://e-cdns-images.dzcdn.net/images/cover/0eb8f51eb7f1d5cbe87b14cb5784d8cb/56x56-000000-80-0-0.jpg","cover_medium":"https://e-cdns-images.dzcdn.net/images/cover/0eb8f51eb7f1d5cbe87b14cb5784d8cb/250x250-000000-80-0-0.jpg","cover_big":"https://e-cdns-images.dzcdn.net/images/cover/0eb8f51eb7f1d5cbe87b14cb5784d8cb/500x500-000000-80-0-0.jpg","cover_xl":"https://e-cdns-images.dzcdn.net/images/cover/0eb8f51eb7f1d5cbe87b14cb5784d8cb/1000x1000-000000-80-0-0.jpg","tracklist":"https://api.deezer.com/album/12337850/tracks","type":"album"},"type":"track"}');
var formattedSong = {
    title: "Unspeakable World",
    artist: "GoGo Penguin",
    img: "https://e-cdns-images.dzcdn.net/images/cover/0eb8f51eb7f1d5cbe87b14cb5784d8cb/56x56-000000-80-0-0.jpg",
    preview: "https://e-cdns-preview-9.dzcdn.net/stream/992022ec4f11760ff134dd41c61c9e99-2.mp3",
    href: "https://www.deezer.com/track/118655608",
    player: "deezer"
};

// Mock for functions that use local storage
class LocalStorageMock {
    constructor() {
        this.store = {};
    }

    clear() {
        this.store = {};
    }

    getItem(key) {
        return this.store[key] || null;
    }

    setItem(key, value) {
        this.store[key] = value.toString();
    }

    removeItem(key) {
        delete this.store[key];
    }
};
global.localStorage = new LocalStorageMock;

var initialPlaylistSongs = '[{"playlist_name":"MaPlaylist","img":"https://i.scdn.co/image/b2c1e92d959f4fc72cd386708bf238edd8aa6fa6","player":"spotify","title":"Kids in Love","artist":"Kygo","preview":"https://p.scdn.co/mp3-preview/676d700bf7b9bf8d520a62b9d7d672c75dce192b?cid=55a904c1f12a42238e0d4b6e10401cfd","href":"https://api.spotify.com/v1/tracks/1Bxzw8ag71snOU9IbhXVD6"}]';
var initialPlaylists = '["MaPlaylist"]';
localStorage.setItem("current_tab", "musicButton");
localStorage.setItem("playlist_musics", initialPlaylistSongs);
localStorage.setItem("playlists", initialPlaylists);

test('generateSongHtml', () => {
  var songHtml = musicScript.generateSongHtml(formattedSong, false);

  expect(songHtml.find("img").length).toBe(1);
  expect(songHtml.find(".musicDesc").length).toBe(1);
  expect(songHtml.find(".music-player").length).toBe(1);
  expect(songHtml.hasClass("song")).toBeTruthy();

  songHtml = musicScript.generateSongHtml(formattedSong, true);

  expect(songHtml.find("button").hasClass("removeFromPlaylist")).toBeTruthy();
});

test('formatSpotifySong', () => {
  var formattedSong = musicScript.formatSpotifySong(spotifySong);

  expect(formattedSong.title).toBe(spotifySong.name);
  expect(formattedSong.artist).toBe(spotifySong.artists[0].name);
  expect(formattedSong.img).toBe(spotifySong.album.images[1].url);
  expect(formattedSong.preview).toBe(spotifySong.preview_url);
  expect(formattedSong.href).toBe(spotifySong.href);
  expect(formattedSong.player).toBe("spotify");
});

test('formatJamendoSong', () => {
  var formattedSong = musicScript.formatJamendoSong(jamendoSong);

  expect(formattedSong.title).toBe(jamendoSong.name);
  expect(formattedSong.artist).toBe(jamendoSong.artist_name);
  expect(formattedSong.img).toBe(jamendoSong.album_image);
  expect(formattedSong.preview).toBe(jamendoSong.audio);
  expect(formattedSong.href).toBe(jamendoSong.shareurl);
  expect(formattedSong.player).toBe("jamendo");
});

test('formatDeezerSong', () => {
  var formattedSong = musicScript.formatDeezerSong(deezerSong);

  expect(formattedSong.title).toBe(deezerSong.title);
  expect(formattedSong.artist).toBe(deezerSong.artist.name);
  expect(formattedSong.img).toBe(deezerSong.album.cover_small);
  expect(formattedSong.preview).toBe(deezerSong.preview);
  expect(formattedSong.href).toBe(deezerSong.link);
  expect(formattedSong.player).toBe("deezer");
});

test('getHashParams', () => {
  var hash = "access_token=BQDtgdlpF18Z1ZV1A8_iARuqrCac0ANWmgbhSwr7WmmU05v6xATUHsiojKPCGvAzTZtsqEfIVHNwGbnaMjUJP9uOyIFDb8dz3DNMtRzCzTC0zytKYd3SGEAMT0T_j-OotPm_FeaDAh05spYipDdDceSz&refresh_token=AQBYTGyHmD42K7XNH8tMQ6cZcVymOt_au1hJ6tRpxFrw-cmrhUaqNkoeYBUdkVP1PElF5nFrvFWrQUvomai4CLHErZRA3WXXK-7YXp-T2e56WaPiHwHkMmaSiqDx25BUxRA";
  expect(musicScript.getHashParams(hash).access_token).toBe("BQDtgdlpF18Z1ZV1A8_iARuqrCac0ANWmgbhSwr7WmmU05v6xATUHsiojKPCGvAzTZtsqEfIVHNwGbnaMjUJP9uOyIFDb8dz3DNMtRzCzTC0zytKYd3SGEAMT0T_j-OotPm_FeaDAh05spYipDdDceSz");
  expect(musicScript.getHashParams(hash).refresh_token).toBe("AQBYTGyHmD42K7XNH8tMQ6cZcVymOt_au1hJ6tRpxFrw-cmrhUaqNkoeYBUdkVP1PElF5nFrvFWrQUvomai4CLHErZRA3WXXK-7YXp-T2e56WaPiHwHkMmaSiqDx25BUxRA");
});

test('removeFromPlaylistItem', () => {
  var playlistItem = '[{"playlist_name":"play","img":"https://i.scdn.co/image/8030f39e07c486071ca737bc8748d71bcd70d1f6","player":"spotify","title":"Mmm Mmm Mmm Mmm","artist":"Crash Test Dummies","preview":"https://p.scdn.co/mp3-preview/5b230e95123f352c3a33b48ce6d081a185a15cad?cid=55a904c1f12a42238e0d4b6e10401cfd","href":"https://api.spotify.com/v1/tracks/31v2AQlx4pDI7kmnLxBkem"}]';
  var song = JSON.parse('{"playlist_name":"play","img":"https://i.scdn.co/image/8030f39e07c486071ca737bc8748d71bcd70d1f6","player":"spotify","title":"Mmm Mmm Mmm Mmm","artist":"Crash Test Dummies","preview":"https://p.scdn.co/mp3-preview/5b230e95123f352c3a33b48ce6d081a185a15cad?cid=55a904c1f12a42238e0d4b6e10401cfd","href":"https://api.spotify.com/v1/tracks/31v2AQlx4pDI7kmnLxBkem"}');

  playlistItem = musicScript.removeFromPlaylistItem(song, playlistItem);
  expect(JSON.parse(playlistItem).length).toBe(0);
});

test('updatePlaylists', () => {
  musicScript.updatePlaylists();
  expect(localStorage.getItem("playlist_musics")).toBe(initialPlaylistSongs);
  expect(localStorage.getItem("playlists")).toBe(initialPlaylists);
});

test('addToPlaylist', () => {
  songHtml = musicScript.generateSongHtml(formattedSong, true);

  expect(musicScript.addToPlaylist(songHtml).find("#choose-playlist").length).toBe(1);
});

test('choosePlaylist', () => {
  songHtml = musicScript.generateSongHtml(formattedSong, true);
  musicScript.choosePlaylist(songHtml, "MaPlaylist");
  var newStoredSongs = JSON.parse(localStorage.getItem("playlist_musics"));

  expect(newStoredSongs.length).toBe(2);
  expect(newStoredSongs[1].playlist_name).toBe("MaPlaylist");
});

test('createPlaylist', () => {
  var form = $('<form id="create-playlist-form"><input name="playlist-name" placeholder="Enter a cool name for your playlist..." required><button id="create-playlist" type="submit"><i class="fa fa-plus-circle"></i> create playlist</button></form>');
  musicScript.createPlaylist(form, "NewPlaylist");
  var newPlaylists = JSON.parse(localStorage.getItem("playlists"));

  expect(newPlaylists.length).toBe(2);
  expect(newPlaylists[0]).toBe("NewPlaylist");

  musicScript.createPlaylist(form, "NewPlaylist");

  expect(newPlaylists.length).toBe(2);
});
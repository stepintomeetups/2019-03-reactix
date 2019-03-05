const MongoClient = require('mongodb').MongoClient;

const client = new MongoClient('mongodb://localhost:27017');

let artistCollection, concertCollection, venueCollection;

let venueIds = {}, artistIds = {};

(async function main() {
    await conectToDatabase();

    await populateVenues();
    await populateArtists();
    await populateConcerts();

    await client.close();
})();

async function conectToDatabase() {
    await client.connect();
    const db = client.db('reactix');

    artistCollection = db.collection('Artist');
    concertCollection = db.collection('Concert');
    venueCollection = db.collection('Venue');
};

async function populateVenues() {
    await venueCollection.deleteMany({});

    const venues = [{
        name: 'Roncsbár',
        location: 'Debrecen'
    }, {
        name: 'Víztorony',
        location: 'Debrecen'
    }, {
        name: 'Rómer Ház',
        location: 'Győr'
    }, {
        name: 'A38 Hajó',
        location: 'Budapest'
    }, {
        name: 'Akvárium',
        location: 'Budapest'
    }, {
        name: 'Helynekem',
        location: 'Miskolc'
    }];
    
    for (const venue of venues) {
        const inserted = await venueCollection.insertOne(venue);

        venueIds[venue.name] = inserted.insertedId.toString();
    }
};

async function populateArtists() {
    await artistCollection.deleteMany({});

    const artists = [{
        name: 'AWS',
        assets: {
            searchCardImage: '/image/artist/aws/aws.jpg', 
            concertSearchImage: '/image/artist/aws/aws.jpg',
            concertImage: '/image/artist/aws/aws.jpg'
        }
    }, {
        name: 'Blahalouisiana',
        assets: {
            searchCardImage: '/image/artist/blahalouisiana/blahalouisiana.jpg', 
            concertSearchImage: '/image/artist/blahalouisiana/blahalouisiana.jpg',
            concertImage: '/image/artist/blahalouisiana/blahalouisiana.jpg'
        }
    }, {
        name: 'Esti Kornél',
        assets: {
            searchCardImage: '/image/artist/esti-kornel/esti-kornel.jpg', 
            concertSearchImage: '/image/artist/esti-kornel/esti-kornel.jpg',
            concertImage: '/image/artist/esti-kornel/esti-kornel.jpg'
        }
    }, {
        name: 'Bagossy Brothers Company',
        assets: {
            searchCardImage: '/image/artist/bagossy-brothers-company/bagossy-brothers-company.jpg', 
            concertSearchImage: '/image/artist/bagossy-brothers-company/bagossy-brothers-company.jpg',
            concertImage: '/image/artist/bagossy-brothers-company/bagossy-brothers-company.jpg'
        }
    }, {
        name: 'Lóci Játszik',
        assets: {
            searchCardImage: '/image/artist/loci-jatszik/loci-jatszik.jpg', 
            concertSearchImage: '/image/artist/loci-jatszik/loci-jatszik.jpg',
            concertImage: '/image/artist/loci-jatszik/loci-jatszik.jpg'
        }
    }, {
        name: 'Sum 41',
        assets: {
            searchCardImage: '/image/artist/sum-41/sum-41.jpg', 
            concertSearchImage: '/image/artist/sum-41/sum-41.jpg',
            concertImage: '/image/artist/sum-41/sum-41.jpg'
        }
    }, {
        name: 'Cold War Kids',
        assets: {
            searchCardImage: '/image/artist/cold-war-kids/cold-war-kids.jpg', 
            concertSearchImage: '/image/artist/cold-war-kids/cold-war-kids.jpg',
            concertImage: '/image/artist/cold-war-kids/cold-war-kids.jpg'
        }
    }, {
        name: 'Twenty One Pilots',
        assets: {
            searchCardImage: '/image/artist/twenty-one-pilots/twenty-one-pilots.jpg', 
            concertSearchImage: '/image/artist/twenty-one-pilots/twenty-one-pilots.jpg',
            concertImage: '/image/artist/twenty-one-pilots/twenty-one-pilots.jpg'
        }
    }];
    
    for (const artist of artists) {
        const inserted = await artistCollection.insertOne(artist);

        artistIds[artist.name] = inserted.insertedId.toString();
    }
};

async function populateConcerts() {
    await concertCollection.deleteMany({});

    const concerts = [{
        time: new Date('2019-03-14T14:56:59.301Z'),
        artistId: artistIds['AWS'],
        title: 'AWS Forradalmi Roncsbár',
        venueId: venueIds['Roncsbár']
    }, {
        time: new Date('2019-04-02T14:56:59.301Z'),
        artistId: artistIds['AWS'],
        title: 'A38-at a Dunába borító zúzás | Vendég: AWS',
        venueId: venueIds['A38 Hajó']
    }, {
        time: new Date('2019-05-22T14:56:59.301Z'),
        artistId: artistIds['AWS'],
        title: 'Szorgalmi időszakot felejtő metálkarácsony | Vendég: Kalapács',
        venueId: venueIds['Akvárium']
    }, {
        time: new Date('2019-06-07T14:56:59.301Z'),
        artistId: artistIds['AWS'],
        title: 'Hajnali járat AWS szülinap',
        venueId: venueIds['Víztorony']
    }, {
        time: new Date('2019-04-02T14:56:59.301Z'),
        artistId: artistIds['Lóci Játszik'],
        title: 'Lóci Játszik a Roncsbárban',
        venueId: venueIds['Roncsbár']
    }, {
        time: new Date('2019-05-16T14:56:59.301Z'),
        artistId: artistIds['Lóci Játszik'],
        title: 'Akusztik Lóci Szederrel',
        venueId: venueIds['Rómer Ház']
    }, {
        time: new Date('2019-06-02T14:56:59.301Z'),
        artistId: artistIds['Lóci Játszik'],
        title: 'Lóci Játszik Krokodil turné',
        venueId: venueIds['Helynekem']
    }, {
        time: new Date('2019-03-28T14:56:59.301Z'),
        artistId: artistIds['Esti Kornél'],
        title: 'Eltűnt Idő turné',
        venueId: venueIds['Roncsbár']
    }, {
        time: new Date('2019-04-14T14:56:59.301Z'),
        artistId: artistIds['Esti Kornél'],
        title: 'Eltűnt Idő turné',
        venueId: venueIds['Helynekem']
    }, {
        time: new Date('2019-05-07T14:56:59.301Z'),
        artistId: artistIds['Esti Kornél'],
        title: 'Sűrű Köd Van | Vendég: Csaknekedkislány',
        venueId: venueIds['Víztorony']
    }, {
        time: new Date('2019-05-20T14:56:59.301Z'),
        artistId: artistIds['Blahalouisiana'],
        title: 'Tales of Blahalouisiana @ Rómer Ház',
        venueId: venueIds['Rómer Ház']
    }, {
        time: new Date('2019-06-12T14:56:59.301Z'),
        artistId: artistIds['Blahalouisiana'],
        title: 'Fesztiválra hangolós Víztorony',
        venueId: venueIds['Víztorony']
    }, {
        time: new Date('2019-05-20T14:56:59.301Z'),
        artistId: artistIds['Blahalouisiana'],
        title: 'Blaha Akusztik',
        venueId: venueIds['Akvárium']
    }, {
        time: new Date('2019-02-20T14:56:59.301Z'),
        artistId: artistIds['Bagossy Brothers Company'],
        title: 'Veled Utazom triplahajó',
        venueId: venueIds['A38 Hajó']
    }, {
        time: new Date('2019-04-19T14:56:59.301Z'),
        artistId: artistIds['Bagossy Brothers Company'],
        title: 'Veled Utazom turné | Vendég: AK26',
        venueId: venueIds['Roncsbár']
    }, {
        time: new Date('2019-06-17T14:56:59.301Z'),
        artistId: artistIds['Sum 41'],
        title: 'Koncert, ami a Budapest Parkban lenne, de azt nem vettem fel',
        venueId: venueIds['Akvárium']
    }, {
        time: new Date('2019-07-28T14:56:59.301Z'),
        artistId: artistIds['Sum 41'],
        title: 'Sum 41 Tribute, amit véletlenül a Sum 41-hoz írtam',
        venue: venueIds['Roncsbár']
    }, {
        time: new Date('2019-03-22T14:56:59.301Z'),
        artistId: artistIds['Cold War Kids'],
        title: 'Cold War Symphoniq ',
        venueId: venueIds['Rómer Ház']
    }, {
        time: new Date('2019-06-28T14:56:59.301Z'),
        artistId: artistIds['Twenty One Pilots'],
        title: 'Ez nem Bécsben lesz?',
        venueId: venueIds['Helynekem']
    }, {
        time: new Date('2019-06-28T14:56:59.301Z'),
        artistId: artistIds['Twenty One Pilots'],
        title: 'Lithium Night Live | TOP',
        venueId: venueIds['A38 Hajó']
    }];

    await concertCollection.insertMany(concerts);
};

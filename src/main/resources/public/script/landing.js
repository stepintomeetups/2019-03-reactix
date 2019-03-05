(function landingIIFE() {
    const selectSearchInput = () => document.querySelector('.artist-search-input');
    const selectCards = () => document.querySelectorAll('.search-feed-container');
    const selectSearchFeed = () => document.querySelector('.search-feed');
    const selectCardTemplate = () => document.querySelector('#artist-search-card');
    const selectCard = card => card.querySelector('.search-feed-container');
    const selectCardBackgroundImage = card => card.querySelector('.bg-img');
    const selectCardTitle = card => card.querySelector('.search-feed-item-title');

    const concertSearchStreamEndpoint = '/api/concerts/search/stream'
    const concertSearchStream = new EventSource(concertSearchStreamEndpoint);

    const maxCardCount = 8;

    (function main() { 
        concertSearchStream.onmessage = searchEventReceived;

        selectSearchInput().addEventListener('keyup', event => {
            if (event.key == 'Enter') {
                const artist = selectSearchInput().value;

                searchForArtist(artist);
            }
        })

        window.addEventListener('beforeunload', windowBeforeUnload);
    })();

    async function searchForArtist(artist) {
        await fetch(`${concertSearchStreamEndpoint}?artistName=${artist}`, {
            method: 'POST'
        });

        window.location = encodeURI(`/concerts/?s=${artist}`);
    };
    
    async function searchEventReceived(event) {
        const artist = JSON.parse(event.data);

        const cardCount = selectCards().length;

        if (cardCount == maxCardCount) {
                selectCards().forEach(card => card.classList.remove('visible'));

                await delay(1000);

                removeCards();
        }

        appendCard(artist);
    };

    function removeCards() {
        const searchFeed = selectSearchFeed();

        while (searchFeed.firstChild) {
            searchFeed.firstChild.remove();
        }
    };

    function appendCard(artist) {
        const card = makeArtistCard(artist);

        selectSearchFeed().appendChild(card);

        setTimeout(() => document.querySelector('.search-feed-container:not(.visible)').classList.add('visible'), 100);
    };

    function makeArtistCard({ name, searchCardImage }) {
        const card = instantiateCardTemplate();
        selectCardBackgroundImage(card).style.backgroundImage = `url("${searchCardImage}")`;
        selectCardTitle(card).textContent = name;

        selectCard(card).addEventListener('click', searchForArtist.bind(null, name));

        return card;
    };

    function instantiateCardTemplate() {
        const template = selectCardTemplate();

        return document.importNode(template.content, true);
    };

    function delay(milliseconds) {
        return new Promise(resolve => setTimeout(resolve, milliseconds));
    };

    function windowBeforeUnload() {
        concertSearchStream.close();

        return null;
    }
})();

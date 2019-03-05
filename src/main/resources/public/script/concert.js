(function concertIIFE() {
    const selectViewCounter = () => document.querySelector(`.other-viewers > .count`);
    const selectTicketButton =  () => document.querySelector('.ticket-button');
    const concertId = document.body.dataset.concertId;

    const ticketPurchaseStreamEndpoint = '/api/tickets/purchase/stream';
    const concertViewStreamEndpoint = '/api/concerts/view/stream';

    const concertViewerStream = new EventSource(`${concertViewStreamEndpoint}?concertId=${concertId}`);

    (function main() {
        concertViewerStream.onmessage = viewEventReceived;

        selectTicketButton().addEventListener('click', ticketButtonClicked);

        window.addEventListener('load', windowLoaded);
        window.addEventListener('beforeunload', windowBeforeUnload);
    })();

    function viewEventReceived(event) {
        const { viewCount } = JSON.parse(event.data);

        setViewCounter(viewCount);
    };

    function ticketButtonClicked() {
        fetch(`${ticketPurchaseStreamEndpoint}?concertId=${concertId}`, {
            method: 'POST'
        });

        alert('A szolgǍĄltatǍĄs nem elǍŠrhetĹ.');

        window.location = '/';
    };

    function windowLoaded() {
        fetch(`${concertViewStreamEndpoint}?concertId=${concertId}`, {
            method: 'POST'
        });
    };

    async function windowBeforeUnload() {
        concertViewerStream.close();

        await fetch(`${concertViewStreamEndpoint}?concertId=${concertId}`, {
            method: 'DELETE'
        });

        return null;
    };

    function setViewCounter(viewCount) {
        const counter = selectViewCounter();

        counter.textContent = viewCount;
    };
})();
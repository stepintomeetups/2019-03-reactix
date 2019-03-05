(function concertSearchIIFE() {
    const selectCounterForConcert = concertId => document.querySelector(`.concert-card[data-concert-id="${concertId}"] .count`);
    const artistId = document.body.dataset.artistId;
    const ticketPurchaseStream = new EventSource(`/api/tickets/purchase/stream?artistId=${artistId}`);

    (function main() {
        ticketPurchaseStream.onmessage = purchaseEventReceived;

        window.addEventListener('beforeunload', windowBeforeUnload);
    })();

    function purchaseEventReceived(event) {
        const purchaseData = JSON.parse(event.data);

        increaseCounterForConcert(purchaseData.concertId);
    };

    function increaseCounterForConcert(concertId) {
        const counter = selectCounterForConcert(concertId);

        counter.textContent = Number.parseInt(counter.textContent) + 1;
    };

    function windowBeforeUnload() {
        ticketPurchaseStream.close();

        return null;
    };
})();
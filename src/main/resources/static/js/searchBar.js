document.addEventListener('DOMContentLoaded', function () {
    const searchInput = document.getElementById('search-input');
    const searchResults = document.getElementById('search-results');
    const errorSuccessBox = document.querySelector('#errorSuccessHandlingBox');
    const errorMessage = document.getElementById('errorMessageDiv');

    searchInput.addEventListener('input', function () {
        const query = searchInput.value.trim();

        if (query.length === 0) {
            searchResults.innerHTML = '';
            return;
        }

        fetch(`/search-live/popular/search-fragment?query=${encodeURIComponent(query)}`)
            .then(response => response.text())
            .then(html => {
                searchResults.innerHTML = html;
            })
            .catch(() => {
                if (errorSuccessBox && errorMessage) {
                    errorSuccessBox.classList.remove('hidden');
                    errorMessage.textContent = 'An error occurred while fetching search results.';
                }
            });
    });
});
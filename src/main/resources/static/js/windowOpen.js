document.getElementById('openLinkButton').addEventListener('click', (event) => {
  const href = event.target.getAttribute('href'); // Get the href attribute of the button
  if (href) {
    window.open(href, '_blank'); // Open the link in a new tab
  } else {
    console.error('No href attribute found on the button');
  }
});
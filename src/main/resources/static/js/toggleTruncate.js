
function toggleTruncate() {
  const element = document.getElementById('toggle_truncate');
  if (element.classList.contains('truncate')) {
    element.classList.remove('truncate');
    element.classList.add('text-wrap-tight');
  } else {
    element.classList.add('truncate');
    element.classList.remove('text-wrap-tight');
  }
}
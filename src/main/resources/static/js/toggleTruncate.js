
function toggleTruncate(element) {
  // Check if the element has the "truncate-multiline" class
  if (element.classList.contains("truncate-multiline")) {
    // Remove the "truncate-multiline" class
    element.classList.remove("truncate-multiline");
  } else {
    // Add the "truncate-multiline" class
    element.classList.add("truncate-multiline");
  }
}
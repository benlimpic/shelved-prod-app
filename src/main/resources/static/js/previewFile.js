
const fileInput = document.getElementById('hiddenFileInput');
const previewImage = document.getElementById('previewImage');
const triggerUpload = document.getElementById('triggerUpload');


fileInput.addEventListener('change', function() {
    const file = this.files[0];

    if (file) {
        const reader = new FileReader();

        reader.onload = function(e) {
            previewImage.src = e.target.result;
        };

        reader.readAsDataURL(file);
    }
});


document.getElementById('triggerUpload').addEventListener('click', function(event) {
  event.preventDefault(); // Prevent the anchor from navigating
  document.getElementById('hiddenFileInput').click();
});
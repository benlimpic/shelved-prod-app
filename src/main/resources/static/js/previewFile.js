
const fileInput = document.getElementById('hiddenFileInput');
const previewImage = document.getElementById('previewImage');
const triggerUpload = document.getElementById('triggerUpload');


fileInput.addEventListener('change', function () {
    const file = this.files[0];

    if (file) {
        const reader = new FileReader();

        reader.onload = function (e) {
            const img = new Image();
            img.onload = function () {
                // Create a canvas to process the image
                const canvas = document.createElement('canvas');
                const ctx = canvas.getContext('2d');

                // Set canvas dimensions (e.g., crop to a square or resize)
                const size = Math.min(img.width, img.height); // Crop to the smallest dimension
                canvas.width = 500; // Resize to 300x300 pixels
                canvas.height = 500;

                // Draw the cropped and resized image onto the canvas
                ctx.drawImage(
                    img,
                    (img.width - size) / 2, // Crop start x
                    (img.height - size) / 2, // Crop start y
                    size, // Crop width
                    size, // Crop height
                    0, // Destination x
                    0, // Destination y
                    canvas.width, // Destination width
                    canvas.height // Destination height
                );

                // Convert the canvas content to a data URL and set it as the preview image source
                previewImage.src = canvas.toDataURL('image/jpeg');
            };

            // Set the image source to the file's data URL
            img.src = e.target.result;
        };

        reader.readAsDataURL(file);
    }
});


document.getElementById('triggerUpload').addEventListener('click', function(event) {
  event.preventDefault(); // Prevent the anchor from navigating
  document.getElementById('hiddenFileInput').click();
});
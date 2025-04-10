

document.addEventListener('DOMContentLoaded', () => {

  const toggleButtonShowInfo = document.getElementById('toggleButtonShowInfo');
  const toggleButtonHideInfo = document.getElementById('toggleButtonHideInfo');
  const itemOverlay = document.getElementById('itemOverlay');
  
  toggleButtonShowInfo.addEventListener('click', () => {
          itemOverlay.classList.remove('hidden');
          toggleButtonShowInfo.classList.add('hidden');
          toggleButtonHideInfo.classList.remove('hidden');
      })

  toggleButtonHideInfo.addEventListener('click', () => {
          itemOverlay.classList.add('hidden');
          toggleButtonShowInfo.classList.remove('hidden');
          toggleButtonHideInfo.classList.add('hidden');
      })

  });

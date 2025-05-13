export function initApp() {
  // ❌ Không gọi $('.main-slider').slick() ở đây nữa!

  // AOS
  AOS.init({ duration: 1200, once: true });

  // Stellar menu
  jQuery('.stellarnav').stellarNav({
    theme: 'plain',
    closingDelay: 250,
  });

  // Tìm kiếm trong header
  $('#header-wrap').on('click', '.search-toggle', function (e) {
    const selector = $(this).data('selector');
    $(selector).toggleClass('show').find('.search-input').focus();
    $(this).toggleClass('active');
    e.preventDefault();
  });

  $(document).on('click touchstart', function (e) {
    if (!$(e.target).closest('#header-wrap .search-toggle, #header-wrap .search-box').length) {
      $('.search-toggle').removeClass('active');
      $('#header-wrap').removeClass('show');
    }
  });


  // SlideNav nếu có
  if (typeof slideNav !== 'undefined' && slideNav.init) {
    slideNav.init();
  }
}

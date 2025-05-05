$(document).on('click', function (e) {
  let target = e.target;

  if (target.closest('.Color .item')) {
    $('.Color .item').removeClass('Color-choose');
    $(target).addClass('Color-choose');
    $('#modal-Color-Choose').modal('hide');
    $('.Color-details').text($(target).attr('data-Color'));
    $('.not-found-Color').hide();
    $('#btn-buy-now').show();
  }
})

$('.Color-guide').click(function () {
  $('body').addClass('modal1-on')
});

$('.go-back-Color-choose').click(function () {
  $('body').addClass('modal2-on')
});

$('#ColorChooseModal').on('hidden.bs.modal', function () {
  $('body').addClass('modal1-on');

  if (!$('.modal.fade').hasClass('show')) {
    $('body').removeAttr('class');
  }
});

$('#ColorGuideModal').on('hidden.bs.modal', function () {
  $('body').addClass('modal2-on');

  if (!$('.modal.fade').hasClass('show')) {
    $('body').removeAttr('class');
  }
});
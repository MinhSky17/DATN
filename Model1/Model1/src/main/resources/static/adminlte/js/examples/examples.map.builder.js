/*
Name: 			Maps / Map Builder - Examples
Written by: 	Okler Themes - (http://www.okler.net)
Theme Version: 	3.1.0
*/

(function($) {

	'use strict';

	var $window = $(window);

	/* Fix Map Color on Mobile */
	function fixMapListener() {

		fixMapColor();

		$(window).on('load reColor orientationchange', function() {
			fixMapColor();
		});

	}

	function fixMapColor() {
		if ( $window.width() <= 767 ) {

			var windowHeight = $(window).height(),
				offsetTop = $('#gmap').offset().top,
				contentPadding = parseInt($('.content-body').css('padding-bottom'), 10);

			$('#gmap').height( windowHeight - offsetTop - contentPadding );
		}
	}

	// auto initialize
	$(function() {

		fixMapListener();

	});

}).apply(this, [jQuery]);
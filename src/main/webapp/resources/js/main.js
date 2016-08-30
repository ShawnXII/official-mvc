(function (factory) {
  if (typeof define === 'function' && define.amd) {
    // AMD. Register as anonymous module.
    define(['jquery'], factory);
  } else if (typeof exports === 'object') {
    // Node / CommonJS
    factory(require('jquery'));
  } else {
    // Browser globals.
    factory(jQuery);
  }
})(function ($) {

  'use strict';

  var console = window.console || { log: function () {} };
  
  function CropAvatar($element) {
	  this.$container = $element;
	  this.$avatar =this.$avatarView.find('img');
	  
	  
   /* this.$avatarView = this.$container.find('.avatar-view');*/
    
    //this.$avatar = this.$avatarView.find('img');
    var title='图片编辑';
	var url='/official-admin/common/uploadFile.htm';
	this.$avatarModal=$('<div class="modal fade" aria-hidden="true" aria-labelledby="avatar-modal-label" role="dialog" tabindex="-1"></div>').css('width',900)
	.css('height',571).css('box-shadow','0 5px 15px rgba(0,0,0,.5)').css('border-radius','6px');
	var lg=$('<div class="modal-dialog modal-lg "></div>').appendTo(this.$avatarModal).css('width',900).css('height',571);
	var content=$('<div class="modal-content"></div>').appendTo(lg);
	this.$avatarForm=$('<form class="avatar-form" action="'+url+'" enctype="multipart/form-data" method="post"></form>').appendTo(content);
	$('<div class="modal-header"><button type="button" class="close" data-dismiss="modal"></button><h4 class="modal-title">'+title+'</h4></div>').appendTo(this.$avatarForm);	
	var body1=$('<div class="modal-body"></div>').appendTo(this.$avatarForm).css('max-height','552px');
	var body=$('<div class="avatar-body"></div>').appendTo(body1);
	this.$avatarUpload=$('<div class="avatar-upload"></div>').appendTo(body);
	this.$avatarSrc =$('<input type="hidden" class="avatar-src" name="avatar_src" />').appendTo(this.$avatarUpload);
	this.$avatarData = $(' <input type="hidden" class="avatar-data" name="avatar_data" />').appendTo(this.$avatarUpload);
	$('<label for="avatarInput">本地上传</label>').appendTo(this.$avatarUpload)
	this.$avatarInput =$('<input type="file" class="avatar-input" id="avatarInput" name="avatar_file">').appendTo(this.$avatarUpload);
	var row=$('<div class="row-fluid"></div>').appendTo(body);
	var row1=$('<div class="span9"></div>').appendTo(row);
	this.$avatarWrapper = $('<div class="avatar-wrapper"></div>').appendTo(row1);
	var row2 =$('<div class="span3"></div>').appendTo(row);
	$('<div class="avatar-preview preview-lg"></div>').appendTo(row2);
	/* $('<div class="avatar-preview preview-md"></div>').appendTo(row1); */
	$('<div class="avatar-preview preview-sm"></div>').appendTo(row2);
	
	this.$avatarBtns =$('<div class="row-fluid avatar-btns"></div>').appendTo(body);
	var btns=$('<div class="span12"></div>').appendTo(this.$avatarBtns);
	var btn_group=$('<div class="btn-group span4"></div>').appendTo(btns);
	$('<button type="button" class="btn blue" data-method="rotate" data-option="-90" title="左旋90°">左旋90°</button>').appendTo(btn_group);
	$('<button type="button" class="btn blue" data-method="rotate" data-option="-15">-15deg</button>').appendTo(btn_group);
	$('<button type="button" class="btn blue" data-method="rotate" data-option="-30">-30deg</button>').appendTo(btn_group);
	$('<button type="button" class="btn blue" data-method="rotate" data-option="-45">-45deg</button>').appendTo(btn_group);
	var btn_group1=$('<div class="btn-group span4"></div>').appendTo(btns);
	$('<button type="button" class="btn blue" data-method="rotate" data-option="90" title="右旋90°">右旋90°</button>').appendTo(btn_group1);
	$('<button type="button" class="btn blue" data-method="rotate" data-option="15">15deg</button>').appendTo(btn_group1);
	$('<button type="button" class="btn blue" data-method="rotate" data-option="30">30deg</button>').appendTo(btn_group1);
	$('<button type="button" class="btn blue" data-method="rotate" data-option="45">45deg</button>').appendTo(btn_group1);
	var sub_div=$('<div class="span3"></div>').appendTo(btns);
	this.$avatarSave = $('<button type="submit" class="btn blue">完成</button>').appendTo(sub_div);
	this.$avatarPreview =row2.find('.avatar-preview');
	this.$avatarModal.modal('show');
    this.init();
  }

  CropAvatar.prototype = {
    constructor: CropAvatar,

    support: {
      fileList: !!$('<input type="file">').prop('files'),
      blobURLs: !!window.URL && URL.createObjectURL,
      formData: !!window.FormData
    },

    init: function () {
      this.support.datauri = this.support.fileList && this.support.blobURLs;

      if (!this.support.formData) {
        this.initIframe();
      }

      this.initTooltip();
      this.initModal();
      this.addListener();
    },

    addListener: function () {
     /* this.$avatarView.on('click', $.proxy(this.click, this));*/
      this.$avatarInput.on('change', $.proxy(this.change, this));
      this.$avatarForm.on('submit', $.proxy(this.submit, this));
      this.$avatarBtns.on('click', $.proxy(this.rotate, this));
    },

    initTooltip: function () {
      /*this.$avatarView.tooltip({
        placement: 'bottom'
      });*/
    },

    initModal: function () {
      this.$avatarModal.modal({
        show: false
      });
    },

    initPreview: function () {
      var url = this.$avatar.attr('src');

      this.$avatarPreview.html('<img src="' + url + '">');
    },

    initIframe: function () {
      var target = 'upload-iframe-' + (new Date()).getTime();
      var $iframe = $('<iframe>').attr({
            name: target,
            src: ''
          });
      var _this = this;

      // Ready ifrmae
      $iframe.one('load', function () {

        // respond response
        $iframe.on('load', function () {
          var data;

          try {
            data = $(this).contents().find('body').text();
          } catch (e) {
            console.log(e.message);
          }

          if (data) {
            try {
              data = $.parseJSON(data);
            } catch (e) {
              console.log(e.message);
            }

            _this.submitDone(data);
          } else {
            _this.submitFail('Image upload failed!');
          }

          _this.submitEnd();

        });
      });

      this.$iframe = $iframe;
      this.$avatarForm.attr('target', target).after($iframe.hide());
    },

    click: function () {
      this.$avatarModal.modal('show');
      this.initPreview();
    },

    change: function () {
      var files;
      var file;

      if (this.support.datauri) {
        files = this.$avatarInput.prop('files');

        if (files.length > 0) {
          file = files[0];

          if (this.isImageFile(file)) {
            if (this.url) {
              URL.revokeObjectURL(this.url); // Revoke the old one
            }

            this.url = URL.createObjectURL(file);
            this.startCropper();
          }
        }
      } else {
        file = this.$avatarInput.val();

        if (this.isImageFile(file)) {
          this.syncUpload();
        }
      }
    },

    submit: function () {
      if (!this.$avatarSrc.val() && !this.$avatarInput.val()) {
        return false;
      }

      if (this.support.formData) {
        this.ajaxUpload();
        return false;
      }
    },

    rotate: function (e) {
      var data;

      if (this.active) {
        data = $(e.target).data();

        if (data.method) {
          this.$img.cropper(data.method, data.option);
        }
      }
    },

    isImageFile: function (file) {
      if (file.type) {
        return /^image\/\w+$/.test(file.type);
      } else {
        return /\.(jpg|jpeg|png|gif)$/.test(file);
      }
    },

    startCropper: function () {
      var _this = this;

      if (this.active) {
        this.$img.cropper('replace', this.url);
      } else {
        this.$img = $('<img src="' + this.url + '">');
        this.$avatarWrapper.empty().html(this.$img);
        this.$img.cropper({
          aspectRatio: 16/9,
          preview: this.$avatarPreview.selector,
          zoomable:false,
          minCanvasWidth:200,
          minCanvasHeight:100,
          strict:true,
          dragCrop:false,
          crop: function (e) {
            var json = [
                  '{"x":' + e.x,
                  '"y":' + e.y,
                  '"height":' + e.height,
                  '"width":' + e.width,
                  '"rotate":' + e.rotate + '}'
                ].join();

            _this.$avatarData.val(json);
          }
        });

        this.active = true;
      }

      this.$avatarModal.one('hidden.bs.modal', function () {
        _this.$avatarPreview.empty();
        _this.stopCropper();
      });
    },

    stopCropper: function () {
      if (this.active) {
        this.$img.cropper('destroy');
        this.$img.remove();
        this.active = false;
      }
    },

    ajaxUpload: function () {
      var url = this.$avatarForm.attr('action');
      var data = this.$avatarForm.serialize();
      var _this = this;
      console.log(data);
      $.ajax(url, {
        type: 'post',
        data: data,
        dataType: 'json',
        processData: false,
        contentType: false,

        beforeSend: function () {
          _this.submitStart();
        },

        success: function (data) {
          _this.submitDone(data);
        },

        error: function (XMLHttpRequest, textStatus, errorThrown) {
          _this.submitFail(textStatus || errorThrown);
        },

        complete: function () {
          _this.submitEnd();
        }
      });
    },

    syncUpload: function () {
      this.$avatarSave.click();
    },

    submitStart: function () {
      this.$loading.fadeIn();
    },

    submitDone: function (data) {
    	console.log(data);
      if ($.isPlainObject(data) && data.state === 200) {
        if (data.result) {
          this.url = data.result;

          if (this.support.datauri || this.uploaded) {
            this.uploaded = false;
            this.cropDone();
          } else {
            this.uploaded = true;
            this.$avatarSrc.val(this.url);
            this.startCropper();
          }

          this.$avatarInput.val('');
        } else if (data.message) {
          this.alert(data.message);
        }
      } else {
        this.alert('Failed to response');
      }
    },

    submitFail: function (msg) {
      this.alert(msg);
    },

    submitEnd: function () {
      this.$loading.fadeOut();
    },

    cropDone: function () {
      this.$avatarForm.get(0).reset();
      this.$avatar.attr('src', this.url);
      this.stopCropper();
      this.$avatarModal.modal('hide');
    },

    alert: function (msg) {
      var $alert = [
            '<div class="alert alert-danger avatar-alert alert-dismissable">',
              '<button type="button" class="close" data-dismiss="alert">&times;</button>',
              msg,
            '</div>'
          ].join('');
      
      this.$avatarUpload.after($alert);
    }
  };

  $(function () {
    return new CropAvatar();
  });

});

/* ===========================================================
 * 图片上传插件
 * 功能点:1.自动匹配页面样式 2.ajax 上传(自动与程序做对接)
 * 3.支持多种上传方案(裁剪,多文件上传,单图片上传)
 * 4.支持后缀过滤
 * 5.支持图片编辑(加滤镜等操作)
 * 6.自动生成dom节点 只需要调用 #id.file 就可以了
 * =========================================================== */
var fileUpload=function(){
	
	//创建modal
	var createModal=function(options){
		var defaultV={
				title:'图片编辑',
		};
		var result=$.extend({},defaultV,options);
		var modal=$('<div class="modal fade" aria-hidden="true" aria-labelledby="avatar-modal-label" role="dialog" tabindex="-1"></div>').css('width','898px').css('height','566px');
		var lg=$('<div class="modal-dialog modal-lg"></div>').appendTo(modal);
		$('<div class="modal-content"></div>').appendTo(lg);
		var form=$('<form class="avatar-form" action="'+url+'" enctype="multipart/form-data" method="post"></form>').appendTo(lg);
		$('<div class="modal-header"><button type="button" class="close" data-dismiss="modal"></button><h4 class="modal-title">'+result.title+'</h4></div>').appendTo(form);	
		var body=$('<div class="modal-body"><div class="avatar-body"></div></div>').appendTo(form);
		var upload=$('<div class="avatar-upload"></div>').appendTo(body);
		$('<input type="hidden" class="avatar-src" name="avatar_src" />').appendTo(upload);
		$(' <input type="hidden" class="avatar-data" name="avatar_data" />').appendTo(upload);
		$(' <label for="avatarInput">本地上传</label><input type="file" class="avatar-input" id="avatarInput" name="avatar_file">').appendTo(upload);
		var row=$('<div class="row"></div>').appendTo(body);
		var row1=$('<div class="col-md-9"></div>').appendTo(row);
		$('<div class="avatar-wrapper"></div>').appendTo(row1).css('width','621px').css('height','364px');
		var row2=$('<div class="col-md-3"></div>').appendTo(row);
		$('<div class="avatar-preview preview-lg"></div>').appendTo(row2);
		/* $('<div class="avatar-preview preview-md"></div>').appendTo(row1); */
		$('<div class="avatar-preview preview-sm"></div>').appendTo(row2);		
		var btns=$(' <div class="row avatar-btns"> <div class="col-md-9"></div></div>').appendTo(body);
		var btn_group=$('<div class="btn-group"></div>').appendTo(btns);
		$('<button type="button" class="btn blue" data-method="rotate" data-option="-90" title="左旋90°">左旋90°</button>').appendTo(btn_group);
		$('<button type="button" class="btn blue" data-method="rotate" data-option="-15">-15deg</button>').appendTo(btn_group);
		$('<button type="button" class="btn blue" data-method="rotate" data-option="-30">-30deg</button>').appendTo(btn_group);
		$('<button type="button" class="btn blue" data-method="rotate" data-option="-45">-45deg</button>').appendTo(btn_group);
		var btn_group1=$('<div class="btn-group"></div>').appendTo(btns);
		$('<button type="button" class="btn blue" data-method="rotate" data-option="90" title="右旋90°">右旋90°</button>').appendTo(btn_group1);
		$('<button type="button" class="btn blue" data-method="rotate" data-option="15">15deg</button>').appendTo(btn_group1);
		$('<button type="button" class="btn blue" data-method="rotate" data-option="30">30deg</button>').appendTo(btn_group1);
		$('<button type="button" class="btn blue" data-method="rotate" data-option="45">45deg</button>').appendTo(btn_group1);
		var sub_div=$('<div class="col-md-3"></div>').appendTo(btns);
		$('<button type="submit" class="btn blue">完成</button>').appendTo(sub_div);
		return modal;
	}
	
	
}();
Brewer = Brewer || {};
Brewer.MultiSelecao = (function() {
	
	function MultiSelecao() {
		this.statusBtn = $('.js-status-btn');
		this.selecaoCheckbox = $('.js-selecao');
	}
	
	MultiSelecao.prototype.iniciar = function() {
		this.statusBtn.on('click', onStatusBtnClicado.bind(this));
	}
	
	function onStatusBtnClicado(event) {
		var botaoClicado = $(event.currentTarget);
		var status = botaoClicado.data('status');
		
		var cheBoxSelecionados = this.selecaoCheckbox.filter(':checked');
		var codigos = $.map(cheBoxSelecionados, function(c){
			return $(c).data('codigo');
		});
		
		
		if(codigos.length > 0){
			$.ajax({
				url: '/brewer/usuarios/status',
				method: 'PUT',
				data:{
					codigos: codigos,
					status: status
				},
				success: function() {
					window.location.reload();
				}
			});
		}
		
	}
	
	return MultiSelecao;
	
}());

$(function() {
	var multiselecao = new Brewer.MultiSelecao();
	multiselecao.iniciar();
});
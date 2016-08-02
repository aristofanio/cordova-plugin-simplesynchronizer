//create interface JS
var AGRequestService = function () {};
//request method
AGRequestService.prototype.request = function(action, callback) {
	cordova.exec(callback, function(e) {
		callback('Error: ' + e);
	}, "SimpleSynchronizerPlugin", "REQUEST", [action])
};
//register method
AGRequestService.prototype.register = function(action, verb, path, callback) {
	cordova.exec(callback, function(e) {
		callback('Error: ' + e);
	}, "SimpleSynchronizerPlugin", "REGISTER", [action, verb, path])
};
//
module.exports = new AGRequestService();

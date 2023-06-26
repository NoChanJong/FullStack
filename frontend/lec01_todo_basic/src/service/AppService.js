import { API_BASE_URL } from '../api-config';

export function call(api, method, request) {
	let options = {
		headers: new Headers({
			"Content-Type": "application/json",
		}),
		url: API_BASE_URL + api,
		method
	};
	if(request) {
		// GET method
		options.body = JSON.stringify(request);
	};

	return fetch(options.url, options).then(res => 
		res.json().then(json => {
			if(!res.ok) {
				return Promise.reject(json);
			}
			return json;
		})
	);
}

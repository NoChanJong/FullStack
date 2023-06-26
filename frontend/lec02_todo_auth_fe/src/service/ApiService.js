import { API_BASE_URL } from '../api-config';

export function call(api, method, request) {

	let headers = new Headers({
		"Content-Type": "application/json"
	});

	// localStorage에서 ACCESS-TOKEN 가져오기
	const accessToken = localStorage.getItem("ACCESS-TOKEN");
	if(accessToken && accessToken !== null) {
		headers.append("Authorization", "Bearer " + accessToken);
	}

	let options = {
		headers: headers,
		url: API_BASE_URL + api,
		method
	};

	if(request) {
		// GET method
		options.body = JSON.stringify(request);
	};

	return fetch(options.url, options).then(res => 
		res.json()
		.then(json => {			

			if(res.status === 200) {
				return res.json();
			}	else if(res.status === 404) {
				window.location.href("/login"); // redirect
			} else {
				new Error(res);
			}
		})
		.catch((error) => {
			console.log(`http:error`);
			console.log(`${error}`);			
		})
	);
}

export function signin(userDTO) {
	return call("/auth/signin", "POST", userDTO)
		.then((response) => {
			if(response.token) {
				// localstorage에 토큰을 저장
				localStorage.setItem("ACCESS-TOKEN", response.token);
				// token이 존재한느 경우 Todo화면으로 redirect
				window.location.href("/");
			}
		})
}

export function signup(userDTO) {
	return call("/auth/signup", "POST", userDTO);
}

export function signout(userDTO) {
	localStorage.setItem("ACCESS-TOKEN", null);
	window.location.href("/login");
}


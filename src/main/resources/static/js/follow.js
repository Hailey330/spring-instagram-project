async function follow(pageUserId){
	let response = await fetch("/follow/" + pageUserId, {
		method: "post"
	});
	let result = await response.text();
	if (result === "OK") {
		location.reload(); // 페이지 새로고침으로 변경하기 - 함수 재사용
	}
}
async function unFollow(pageUserId){
	let response = await fetch("/follow/" + pageUserId, {
		method: "delete"
	});
	let result = await response.text();
	if (result === "OK") {
		location.reload();
	}	
}
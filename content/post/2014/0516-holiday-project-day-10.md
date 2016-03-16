---
title: '프로젝트 10일: Async.js로 콜백 중첩을 풀기'
kind: article
date: 2014-05-16
layout: holiday-project
url: holiday-project-day-10
---

나는 30일의 휴가 중이다. 휴가 동안 개인 개발 프로젝트를 진행하고 있고, 오늘은 그 열흘째다.

[Async.js]
---------

[어제 Node.js로 Git 저장소의 커밋 오브젝트를 찾아보면서, 콜백 폭포에 대한 어색함을 토로했다.](/holiday-project-day-09/) 출근하면, 회사 동료들에게 관련 내용을 물어보려 했는데, 오늘 마침 회사에 잠깐 들를 일이 있었다. 딸내미 100일 떡을 돌리라는 명(?)을 받들어 회사에 가서 막 쪄온 떡을  돌리고, 오랜만에 본 팀원 분들과 얘기를 나누며 관련 문의를 했고, 곧바로 속 시원하게 들은 해결안은 [Async.js]를 사용해보라는 것!

집에 와서 살펴보니, [Async.js]는 여러 가지 비동기 호출에 편리한 기능을 제공하는데, 그중에서 [waterfall] 함수를 사용하면 자꾸 중첩되는 콜백 함수들을 명시적으로 표현해 호출하기에 좋다.

```js
async.waterfall(tasks, [callback])
```

waterfall 함수는 ```tasks``` 배열과, 에러 ```callback```을 파라미터로 받는다. ```tasks``` 배열에 넣어 전달하는 각각의 함수는 차례로 실행되며, 그 전에 실행된 함수에서 호출한 ```callback``` 함수를 통해 다음 함수로 전달된다. 만약 실행된 함수에서 ```callback```의 첫 번째 파라미터인 ```err```에 ```null```이 아닌 값을 넣어 호출한다면 -- 즉 에러가 발생하면 -- 그 뒤의 함수들은 호출되지 않고, waterfall의 두 번째 파라미터였던 에러 ```callback```이 실행되면서 err 값이 전달된다.

내용도 어렵고, 내 표현력도 부족해서 이해하기 쉽지 않다. 사이트의 예제를 그대로 가져와 다시 설명하자.

### [waterfall] 예제

```javascript
async.waterfall([
  function(callback){
    callback(null, '하나', '둘');
  },
  function(arg1, arg2, callback){
    // arg1는 '하나'고, arg2는 '둘'이다.
    callback(null, '셋');
  },
  function(arg1, callback){
    // arg1은 '셋'이다.
    callback(null, '끝');
  }
], function (err, result) {
   // result에는 '끝'이 담겨 온다.
});
```

위 예제에서 waterfall 함수의 첫 번째 파라미터 ```tasks```에 익명 함수 3개를 전달했고, 마지막 콜백 함수도 표현돼 있다.

1. 첫 번째 익명 함수는 처음 실행되는 함수이기에, 별도 파라미터 없이, callback 함수만 파라미터로 받았고, 그 함수를 곧바로 실행했으며, 첫 번째 파라미터가 ```null```이므로, 문제없이 두 번째 익명 함수를 호출한다. 그리고, 그때 전달하는 인자는 ```하나```와 ```둘```이 된다.

1. 두 번째 익명 함수는, 첫째 함수에서 callback 함수를 호출하며 파라미터로 전달한 ```하나```와 ```둘```을 각각 arg1과 arg2로 받았고, 이 익명 함수에서는 호출하는 callback에는 ```null```, ```셋```을 전달하므로, 정상적으로 세 번째 익명 함수를 호출한다.

1. 마지막으로, 세 번째 익명 함수는, ```셋```을 받은 뒤, 마지막 callback으로 ```끝```을 담아 호출한다. 그러면 최종적으로 waterfall의 두 번째 파라미터인 callback 함수에 err는 null, result에는 ```끝```을 담아 호출한다.

만약 여기서 두 번째 익명함수를 살짝 바꿔서 아래와 같이 호출한다면 어떻게 될까?

```js
  // 전략
  function(arg1, arg2, callback){
    // arg1는 '하나'고, arg2는 '둘'이다.
    callback("어떤 에러", '셋');
  },
  // 후략
```

이럴 경우, callback의 err 파라미터가 null이 아니므로, **세 번째 익명 함수를 호출하지 않은채**, 마지막 콜백 함수가 호출되며, 해당 콜백에는 ```어떤 에러```와 ```셋```을 전달한다.

조금 복잡하긴 하지만, node.js에서 흔히 쓰는 "중첩된 비동기 호출"을 깔끔히 처리하기 좋은 방법이다.

어제의 콜백 폭포를 다시 쓴다면...
-----------------------------

[어제]의 코드를 다시 보자. [nodegit]을 이용해 Git 저장소를 열고, 특정 커밋 객체를 찾아보는 테스트 코드다. 이번에는 커피스크립트 코드로 살펴보자.

### [repo_spec.coffee](https://github.com/hatemogi/karma-practice/blob/day-10/spec/nodegit/repo_spec.coffee)

어제의 원래 코드이고, 콜백이 여러번 중첩됐다.

```coffeescript
nodegit = require("nodegit")

describe '[CoffeeScript] nodegit 저장소', () ->
  it '열어서 커밋 찾아보기', (done) ->
    sha = "e9ec116a8fb2ea051a4c2d46cba637b3fba30575"
    nodegit.Repo.open "git/nodegit", (err, repo) ->
      return done(err) if err
      expect(repo.path()).toMatch /\.git\/$/
      repo.getCommit sha, (err, entry) ->
        return done(err) if err
        expect(entry.sha()).toEqual sha
        done()
```

원래 코드는 저장소를 open 함수로 열고, 에러 발생 여부를 확인하고, 커밋 객체를 찾아보고, 또 에러 발생 여부를 확인한 뒤, 마지막으로 jasmine 테스트 완료를 통보(callback)한다. 코드 중간마다 에러 발생 여부를 확인하는 코드와 더는 실행하지 않게 하는 ```return``` 구문이 필요하다. (일반 코드라면 throw로 확실하게 중단 하는 것이 좋겠지만, jasmine에 에러 내역을 ```done(err)``` 함수로 전달하기 위해 return으로 종료했다.)

### [repo_async_spec.coffee](https://github.com/hatemogi/karma-practice/blob/day-10/spec/nodegit/repo_async_spec.coffee)

아래는, async.[waterfall]로 다시 쓴 코드다.

```coffeescript
nodegit = require("nodegit")
async = require("async")

describe '[CoffeeScript w/async.js] nodegit 저장소', () ->
  it '열어서 커밋 찾아보기', (done) ->
    sha = "e9ec116a8fb2ea051a4c2d46cba637b3fba30575"
    async.waterfall [
      (callback) ->
        nodegit.Repo.open "git/nodegit", callback
      (repo, callback) ->
        expect(repo.path()).toMatch /\.git\/$/
        repo.getCommit sha, callback
      (entry, callback) ->
        expect(entry.sha()).toEqual sha
        callback null
    ], (err, _result) -> done(err)
```

waterfall로 작성한 코드는 위와 같다. 콜백 **폭포(중첩)**를, 익명 함수의 **나열**로 간소화했으며, 중간마다 에러 발생 여부를 검사하는 코드도 보이지 않아서, 논리적 흐름을 거침없이 볼 수 있다. 공교롭게도(?) 코드 라인 수는 늘어났기에, 이 경우 확실히 waterfall을 사용한 것이 낫다고 주장하기 어려운 면도 있지만, 개인적으로는 전체 흐름을 한 눈에 파악하기 쉽기 때문에 종종 사용하게 될 것 같다.

[waterfall] 말고도, [Async.js]에 비동기 함수 호출을 하는 데 편리한 함수들이 많아서, 틈틈히 참고해가며 코딩하기로 한다.

오늘은 여기까지.

[어제]: /holiday-project-day-09/
[nodegit]: http://www.nodegit.org/
[Async.js]: https://github.com/caolan/async
[waterfall]: https://github.com/caolan/async#waterfall
[Promises.js]: https://www.promisejs.org/

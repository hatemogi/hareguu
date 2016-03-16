---
title: '프로젝트 22일: Promise와 Async.js로 작성해본 콜백 코드 비교'
date: '2014-05-31'
layout: 'holiday-project'
url: 'holiday-project-day-22/'
description: '자바스크립트 자체만으로도 그렇지만, 특히 Node.js 환경은 비동기 I/O가 중요해서, 콜백 방식의 함수 호출이 자주 활용되며, 그 호출간의 중첩도 잦다. 즉, 콜백에서 다시 콜백 걸고 또 콜백 거는, 계속 중첩되는 콜백이 필요한 경우가 많다. 이 연속되는 콜백을 그냥 평범하게 코딩하면, 마치 예외처리 (try-catch-finally) 구문 없이 코딩하는 것처럼, 예외 처리에 대한 코드가 중간중간 끼어들어서 정상적인 로직 코드가 묻혀버리기 쉽다. Promise와 Async.js로 이 문제를 해결해보자.'
---
 
오늘은 프로젝트 22일째 작성한 Async.js 코드와 Promise 코드를 비교해본다.

콜백 중첩의 늪
--------------

자바스크립트 자체만으로도 그렇지만, 특히 Node.js 환경은 비동기 I/O가 중요해서, 콜백 방식의 함수 호출이 자주 활용되며, 그 호출 간의 중첩도 잦다. 즉, 콜백에서 다시 콜백 걸고 또 콜백 거는, 계속 타고들어가는 콜백이 필요한 경우가 많다. 

이 연속되는 콜백을 그냥 평범하게 코딩하면, 마치 예외처리 (try-catch-finally) 구문 없이 코딩하는 것처럼, 예외 처리에 대한 코드가 중간중간 끼어들어서 정상적인 로직 코드가 묻혀버리기 쉽다. 

[10일째 작성했던 코드를 다시 예로 들어보자.](/holiday-project-day-10/)

```js
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

보통의 비동기 콜백 함수들이 파라미터로 ```(err, result)```를 받는 형태이고, 처리 과정에서 에러가 발생하면 err에 에러 관련 값이 들어오고, 정상처리됐으면 null이 온다. 그리고, result에 결과값이 담겨있는 형태다.

위 코드의 중간마다 있는 ```return done(err) if err``` 부분이 에러 상황에 Jasmine 프레임워크에 에러 결과를 리포팅하겠다는 코드인데, 콜백 도입부마다 똑같은 코드를 넣어서 전체 코드가 눈에 잘 띄지 않게 됐다.

Promise와 Async.js
------------------

이 불편함을 해결하는 방법이 여럿 있을 것 같은데, 그중에서도 Async.js와 Promise를 살펴봤다. Async.js는 비동기 호출에 편리한 유틸리티 함수들이 제공되는 라이브러리여서, npm등으로 잘 가져다가 사용하면 된다. 평범한 자바스크립트 라이브러리라 브라우저에서 사용해도 된다. 한편, Promise는 사실 자바스크립트(ECMAScript) 스펙에 포함되는 규약인데, 아직 지원하지 않는 자바스크립트 엔진을 위해, 별도 구현체 중에 하나 가져다가 쓰면 된다. 

콜백 방식의 호출은 물론, Promise와 Async.js도 익숙치 않기 때문에, 연습해볼 겸 똑같은 코드를 둘다의 방식으로 작성해봤다. nodegit으로 git 저장소를 열고, 커밋 두 개에 엮인 트리를 찾아서 둘 사이 차이(패치 크기)를 알아보는 메소드를 작성했고, 코드는 아래와 같다.

### [Async로 작성한 코드](https://github.com/hatemogi/holiday-project/blob/day-22/spec/nodegit/repo_async_spec.coffee)
```js
nodegit = require("nodegit")
async = require("async")
_ = require("underscore")

nodegitPath = ".git/modules/git/nodegit/"

describe '[CoffeeScript w/async.js] nodegit 저장소', () ->
  it '열어서 커밋 찾아보기', (done) ->
    sha = "e9ec116a8fb2ea051a4c2d46cba637b3fba30575"
    open = nodegit.Repo.open
    async.waterfall [
      open.bind(open, nodegitPath)
      (repo, cb) ->
        expect(repo.path()).toMatch /\/git\/nodegit\/$/
        repo.getCommit sha, cb
      (entry, cb) ->
        expect(entry.sha()).toEqual sha
        cb null
    ], done

  it 'diff 실행해보기', (done) ->
    async.waterfall [
      (cb) ->
        nodegit.Repo.open nodegitPath, cb
      (repo, cb) ->
        async.parallel [
          repo.getCommit.bind(repo, "33c7b930acc13148ef6f05df56f9b8a5c3578a57"),
          repo.getCommit.bind(repo, "c3e4be4448d2a99917431d3be972ca262805f989")
        ], (err, commits) -> cb(err, repo, commits)
      (repo, commits, cb) ->
        async.parallel [
          repo.getTree.bind(repo, commits[0].treeId()),
          repo.getTree.bind(repo, commits[1].treeId())
        ], cb
      (trees, cb) ->
        trees[0].diff trees[1], cb
      (diff, cb) ->
        expect(_.reduce(diff.patches(), ((m, p) -> m + p.size()), 0)).toBe(2)
        cb null
    ], done
```
 
```async.waterfall```과 ```async.parallel``` 함수를 사용해서 작성했고, 전체적 흐름은 일관되고 편리하다. 하지만, 콜백 꼬리 ```cb```를 늘 달고 다녀야 하는 아쉬움(?)이 있다. 

### [Promise로 작성한 코드](https://github.com/hatemogi/holiday-project/blob/day-22/spec/nodegit/repo_promise_spec.coffee)
```js
nodegit = require("nodegit")
Promise = require("promise")
_ = require("underscore")

nodegitPath = ".git/modules/git/nodegit/"

openRepo = Promise.denodeify(nodegit.Repo.open)
getCommit = (repo) ->
  Promise.denodeify(repo.getCommit.bind(repo))
getTree = (repo) ->
  Promise.denodeify(repo.getTree.bind(repo))
getDiff = (tree) ->
  Promise.denodeify(tree.diff.bind(tree))

describe '[CoffeeScript w/promise.js] nodegit 저장소', () ->
  it '열어서 커밋 찾아보기', (done) ->
    sha = "e9ec116a8fb2ea051a4c2d46cba637b3fba30575"
    openRepo(nodegitPath).then((repo) ->
      expect(repo.path()).toMatch /\/git\/nodegit\/$/
      getCommit(repo)(sha)
    ).then((entry) ->
      expect(entry.sha()).toEqual sha
    ).then done, done

  it 'diff 실행해보기', (done) ->
    openRepo(nodegitPath).then((repo) ->
      Promise.all([
        getCommit(repo)("33c7b930acc13148ef6f05df56f9b8a5c3578a57"),
        getCommit(repo)("c3e4be4448d2a99917431d3be972ca262805f989")
      ]).then((c) ->
        Promise.all [
          getTree(repo)(c[0].treeId()),
          getTree(repo)(c[1].treeId())
        ]
      )
    ).then((trees) ->
      getDiff(trees[0])(trees[1])
    ).then((diff) ->
      patchsize = _.reduce(diff.patches(), ((m, p) -> m + p.size()), 0)
      expect(patchsize).toBe(2)
    ).then done, done
```

Promise의 경우, 전체 메시지를 마치 보통 동기 호출 스타일로 작성하자는 것이 포인트인데, 어떤 비동기 처리 뒤에 ```then``` 함수를 붙여서, 성공한 경우와 실패한 경우의 처리를 한다. 계속 연결해서 (chaining) 사용하는 것이 핵심이라면 핵심이다. 

위 코드는 ```Promise.then```과 ```Promise.all```을 사용했다. 결국 두 코드 스타일이 비슷해졌다. 팀 동료들에게 물어보니, Async가 더 보기 좋다는 사람도 있던데, 내 경우에는 Promise가 좋게 느껴지는 면도 있다고 생각한다. 우선 늘 쫓아다녀야 하는 콜백 함수 꼬리표를 떼놓고 생각할 수 있고, 코드의 흐름이 차례로 흐르는 방식이 평소 동기화 방식 코딩을 할 때와 크게 다르지 않아서 좋다. 

그러나, 둘 중 하나를 쓴다면...
-------------------

Async.js의 경우, 비동기 호출과 관련한 다양한 유틸리티가 많아서, 1:1로의 비교는 어렵지만, waterfall에 한정 지어 비교해본다면 결론이 쉽게 난다.

Promise 스타일이 내게는 더 좋지만, 문제는, 이미 널리 있는 자바스크립트 라이브러리들이나 Node.js의 기본 API들이 일반 콜백 방식으로만 되어있지, Promise 스타일로 준비된 것이 아니어서, 위 코드에서처럼 ```Promise.denodeify```같은 유틸리티 함수로 Promise화 한다거나, 아니면 직접 Promise 함수로 만들어 놔야 한다는 단점이 있다. 

한마디로, 이미 통용되는 코드를 그대로 활용하기에는 Async.js가 좋은 것 같다. Promise 스타일이 더 널리 쓰이기 시작해서 다른 API들도 그 스타일대로 쓸 수 있게 되면 적극적으로 활용해보기 좋다고 생각한다. 그전까지는 Async.js의 활용도가 더 높을 수밖에 없을 것 같다. 


한편, 콜백의 불편함을 감수해야 하나?
-------------------------------

한편, 조금 원론적인 얘기로, 비동기 코딩으로 왜 콜백의 불편함을 감수해야 할까? 자바스크립트 코딩할 때나, Node.js 코딩에서도 마찬가지로 그냥 동기화 방식으로 코딩하면 안 될까? 물론 된다. 하지만, 동기화 함수를 만날 때마다 블럭킹이 일어나면, 다른 코드가 동시에 실행될 수 없으므로, 클라이언트 코드의 경우 화면 응답성 등이 떨어질 것이고, 서버 코드의 경우 동시에 여러 사용자 처리를 할 수 없을 것이다. 

즉, 한마디로 동시성(concurrency) 확보를 위해 비동기 I/O 호출을 하게 되는 것인데, 사실 따지고 보면 기존에 JAVA 등으로 멀티쓰레딩 처리를 했던 것도 마찬가지로 동시성 확보를 위해 하는 일인데, 그 멀티 쓰레드 처리를 잘하기 위해 임계영역(critical section)을  잘 관리해야 하며, 신경 써야 할 점도 많다.

어찌 보면 멀티쓰레드로 개발하는 것이, 당장엔 편해 보이지만 자칫하면 동시 사용자가 몰렸을 때 제대로 처리되지 않아 쓰레드 안정성(thread-safety)이 깨진 경우의 문제가 발생한다거나 하는 심각한 문제로의 발전 가능성이 있다. 그런 면에서는 콜백 처리의 불편함쯤이야 한번 감수하면 뒤탈은 적은 방식일지도 모른다. 

이상으로, Promise와 Async.js로 **콜백 중첩 문제**를 해결해보았다.

오늘은 여기까지.








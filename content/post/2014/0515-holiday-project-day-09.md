---
title: '프로젝트 9일: Node.js로 Git저장소 다루기'
kind: article
date: 2014-05-15
layout: holiday-project
url: holiday-project-day-09
---

나는 30일간의 안식휴가 중이다. 휴가 동안 개인 개발 프로젝트를 진행하고 있고, 오늘은 그 9일째다.

[GitHub API](https://developer.github.com/v3/)
-----------

오전에, [GitHub API: Getting Started](https://developer.github.com/guides/getting-started/)를 비롯해, API 설명서를 읽었다. 프로젝트에서 사용할 기능을 깃헙이 제공하는 API를 써서 구현 가능한지 판단하는 것이 주요목표였고, 결론을 먼저 말하면, 당연히 사용할 수 있었다. 문제없이 진행할 수 있겠다. 

살펴보면서 느낀 점은 "깃헙 개발자 API 설명이 참 잘 돼있다"는 것이다. 눈으로 쭉 읽기만 해도 어떻게 돌아갈지 잘 와 닿았고, 특히 그 어렵고 복잡한 OAuth 테스트를 위해서 [개발자용 토큰을 웹에서 발급](https://help.github.com/articles/creating-an-access-token-for-command-line-use)  받을 수 있다는 점이 마음에 들었다. 일일이 OAuth 콜백용 서버를 준비하고 띄워두고 로그인해서 인증처리까지 하고 나서야 토큰 하나 받아서 그 이후 테스트를 해볼 수 있는 게 아니라, 개발용 토큰부터 미리 받아서 실제 로직 테스트부터 진행할 수 있다. 매력적이다.

나머지 API 문서도 살펴보며, 따라 해 볼 만한 것은 따라 해보고, GitHub API 답사를 마쳤다. 답사의 결론은 "필요로 하는 기능을 깃헙 API로 구현할 수 있다".

[nodegit]
--------

오후에는 바닷가에 잠시 놀러 갔다 왔고(휴가중이니까!), 밤이 돼서야 [nodegit]을 연습해보기 시작했다. [nodegit]은 node.js용 Git 라이브러리인데 내부적으로 C로 작성된 [libgit2]를 사용한다. 'Git 라이브러리의 갑'인 libgit2 사이트에 보면, Node.js용 바인딩으로는 [다른 라이브러리](https://github.com/libgit2/node-gitteh)가 참조돼있는데, 해당 라이브러리 사이트에 가보면, 최근 활동이 활발하지 않고 [새로운 소유자를 찾고 있다](https://github.com/libgit2/node-gitteh/issues/68). 따라서, 지금은 node.js용 라이브러리로는 [nodegit]이 쓸만한 상황으로 보인다.

루비에서 Git 저장소 관련 작업을 할 때, 순수 루비로 짠 [Grit]도 써보고, 네이티브 libgit2를 바인딩한 [Rugged]도 써봤다. Grit은 후에 유지보수가 되지 않았고, Rugged는 초기 버전을 쓸 때는 툭하면 Segmentation Fault를 떨어뜨렸다. 물론 지금의 Rugged는 매우 훌륭해서 문제 될 것이 없다. 하지만, nodegit은 아직 초기 버전이라, Rugged의 초기 버전 때처럼 SegFault를 만나게 될까 걱정이 앞서지만, 현재는 다른 대안이 없는 것 같다.

걱정은 일단 접어두고, [프로젝트 둘째 날](/holiday-project-day-02/) 알아본 jasmine-node에서 돌릴 테스트케이스(스펙)를 작성해봤다. 로컬의 ```git/nodegit```에 nodegit 저장소를 클론 받아놨고, nodegit을 이용해 해당 저장소의 커밋을 하나 읽어 보는 테스트케이스다.

### repository_spec.js
```js
describe('nodegit 저장소', function() {
  var nodegit = require("nodegit");

  it('열어서 커밋 찾아보기', function(done) {
    nodegit.Repo.open("git/nodegit", function(err, repo) {
      if (err) {
        return done(err);
      }
      expect(repo.path()).toMatch(/\.git\/$/);

      var sha = "e9ec116a8fb2ea051a4c2d46cba637b3fba30575";
      repo.getCommit(sha, function(err, entry) {
        if (err) {
          return done(err);
        }
        expect(entry.sha()).toEqual(sha);
      });
    });

    done();
  });
});
```

자바스크립트가 익숙하지 않은 건지, 노드에서 많이 활용되는 콜백 스타일 호출이 어색한 것인지, 아직 코드를 작성하기도 어색하고, 맞게 작성하고 있는 건지 알 수가 없다. 하는 일에 비해, 너무 소스 코드가 길어서 살짝 심기가 불편하다.

```bash
jasmine-node --verbose spec
```

어쨌건 위의 명령어로, 그전까지의 테스트케이스와 함께 돌려 볼 수 없고, 문제없이 실행되는 것을 확인했다.

혹시, [커피스크립트](http://coffeescript.org)로 작성해보면 조금 보기에 나아질까 하는 궁금증이 일어, 곧바로 작성해봤다.

```coffeescript
describe '[CoffeeScript] nodegit 저장소', () ->
  Repo = require("nodegit").Repo

  it '열어서 커밋 찾아보기', (done) ->
    Repo.open "git/nodegit", (err, repo) ->
      return done(err) if err
      expect(repo.path()).toMatch /\.git\/$/
      sha = "e9ec116a8fb2ea051a4c2d46cba637b3fba30575"
      repo.getCommit sha, (err, entry) ->
        return done(err) if err
        expect(entry.sha()).toEqual sha
    done()
```

똑같은 테스트를 하는 커피스크립트 코드인데, 코드 줄 수는 짧아졌지만, 여전히 뭔가 불편한 것이... 그냥 아직 콜백이 자꾸 겹치는 스타일에 익숙치 않은 것 같다.

```bash
jasmine-node --verbose --coffee spec
```

참고로, 위의 명령어처럼 jasmine-node를 실행할 때, ```--coffee``` 옵션을 주면, 테스트케이스 파일 중에 확장자가 ```.coffee```인 파일을 자동으로 자바스크립트로 변환해서 함께 실행해 준다

몇 달 전쯤에, 회사에서 누군가 이런 콜백 폭포수 코딩의 불편함을 하소연하는 걸 옆에서 들었는데, 다른 옆사람 말이, 또 그런 걸 해결해주는 라이브러리가 있다고 했다. 그때야 관심이 없어서 더 묻지도 않았는데, 이제야 새삼 궁금해지니, 나중에 출근하면 한번 확인해보려 한다.

그때까지는 일단 저 콜백 스타일에 좀 적응해보자. 사실 뭐 if-else가 겹치는 거나 콜백이 겹치는 거나, 결과적으로 겹치는 건 겹치는 거 아닌가?!

오늘은 여기까지.

[nodegit]: http://www.nodegit.org/
[libgit2]: http://libgit2.github.com/
[Grit]: https://github.com/mojombo/grit
[Rugged]: https://github.com/libgit2/rugged
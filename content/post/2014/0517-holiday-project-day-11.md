---
title: '프로젝트 11일: Git 커밋 그래프 그리기 도전'
kind: article
date: 2014-05-17
layout: holiday-project-d3js
url: holiday-project-day-11
---
나는 30일 동안 휴가 중이다. 휴가 동안 개인 개발 프로젝트를 진행하고 있다.

### [D3.js]의 Layout 기능

오늘 새벽 5시쯤 일찍 잠에서 깼는데 다시 잠이 오지 않아, 침대에 누운 채로 [D3.js] 문서를 좀 읽었다. [Force Layout] 부분을 보아하니, 원래 계획하던 그래프를 그리는 데에 사용할 수 있을 것 같은 느낌이 왔다. 문서를 마저 읽고, 설레는 마음을 품고, 책상에 앉아서 코딩을 시작했고, 잠깐의 몰입 후에 기본 코드가 완성됐다. 개발이 즐거운 이유는, 몰입(flow)의 경험이 가장 크지 않나 싶다. 피드백을 곧바로 확인할 수 있고, 적절히 도전적인 과제가 있다.

> 오, 좋았어! 이 코드를 사용해서 오늘 멋진 그래프를 그릴 수 있을 것 같아!

기대감과 설렘을 담아둔 채, 다른 (놀이) 일정을 위해 집을 나섰다. 기대감 덕분에 목적지를 오가는 운전하는 시간이 전혀 지겹지 않았을 정도다. 오후에 집으로 돌아와서 조금 쉬다가, 저녁이 돼서야 다시 본격적인 코딩을 시작했다.

### Git 그래프 그리기

하고자 하는 작업은 Git 저장소의 커밋 이력을 그래프로 보여주는 작업. 흔히 쓰는 GUI 툴에서 보여주는 것이나, 터미널에서 ```git log --graph --oneline```처럼 ```--graph``` 옵션으로 보는 그래프 말이다.

<img src="/img/post/holiday-project/git-graph-sourcetree.png" class="img-rounded" style="width: 400px;"/>

위 이미지 중앙의 그래프 같은 것. 보통 저 그래프는 커밋과 브랜치의 흐름을 *부차적으로* 확인하기에 좋은데, 저 정보를 **중심으로 바라보는** 그래프를 그려보고 싶었다. 시간의 흐름 순으로 위에서 아래나, 왼쪽에서 오른쪽으로 표현하는 것이 가장 일반적인데, 조금 더 자유롭게 표현해보면 어떨까 하는 호기심이 일었고, 새벽에 보았던 D3.js의 [Force Layout]를 써서 그려봤다.

[D3.js]: http://d3js.org
[Force Layout]: https://github.com/mbostock/d3/wiki/Force-Layout

<style>
  circle {
    stroke: black;
    stroke-width: 1.5px;
    fill: steelblue;
    cursor: pointer;
  }
  line {
    stroke: darkgrey;
    stroke-width: 1.5px;
  }
  text {
    fill: white;
    text-anchor: middle;
    cursor: pointer;
  }
  path {
    stroke: darkgrey;
    stroke-width: 2px;
    fill: none;
  }
</style>

<section class="button-section">
  <button class="pure-button pure-button-primary pure-button-lg show-button">어디 한번 봅시다!</button>
  이 버튼을 누르면, 오늘 그린 그림이 보인다.
</section>

<div class="graph-section" style="display: none;">
  <h3>어떤 Git 저장소의 master 브랜치에 있는 커밋들</h3>
  <svg class="d3"></svg>
  <p>
    <button class="btn btn-warning resume-button">흔들어봅시다!</button>
  </p>
  <div class="panel panel-info">
    <div class="panel-heading">
      원을 클릭해 보세요.
    </div>
    <div class="panel-body">
      커밋메시지가 보일 거에요.
    </div>
  </div>
</div>

> <https://github.com/nodegit/nodegit>

볼만한가? 버튼을 누르면 드러나는 그래프는 [nodegit] 저장소의 ```master``` 브랜치의 모든 커밋 정보를 가져와서, JSON 파일로 만들어 둔 뒤, D3.js의 Force Layout을 이용해 그래프로 표현한 것이다. 각각의 원이 하나의 커밋을 표현한 것이며, 커밋에 반영된 코드 변화분이 크면 클수록 원의 지름도 크게 표현했다. 원을 드래그해서 조금 위치를 흔들어 볼 수도 있다.

별도의 위치 지정이나 텐션 조절을 하지 않은 기본값이며, 제대로 그리려면, 몇 가지 튜닝도 하고 다른 브랜치와의 관계도 보여야 할 것이며, 모든 커밋이 아니라 보여줘서 의미 있는 최근의 커밋들만 보이게 한다거나 하는 일이 필요하다.

아쉽게도 큰 기대에 비해서 실망스러웠다. 이건 그냥 잔뜩 엉켜있는 그래프잖아. 어쩌면, 단순히 D3.js에 대충 데이터 던져주고 멋지게 그려주기를 바랐는지도 모른다. 새벽과 오전 내내의 설렘이, 비눗방울 터지듯 훅 가버렸지만, 계속 개선해보면 좀 멋진 그래프를 그릴 수 있을 거라고 스스로 위안해 본다.

참고로, 아래는 위의 커밋 정보들을 JSON으로 뽑아낸 커피스크립트 코드다.

#### [repo_dump.coffee](https://github.com/hatemogi/karma-practice/blob/day-11/lib/repo_dump.coffee)

```coffeescript
# ... 앞 코드 생략
commits = {}
branches = {}

async.waterfall [
  (cb) ->
    git.Repo.open 'git/nodegit.git', cb
  (repo, cb) ->
    repo.getMaster cb
  (branch) ->
    history = branch.history(git.RevWalk.Sort.Topological)
    branches["master"] ||= []
    history.on 'commit', (commit) ->
      parents = _.map(commit.parents(), (c) -> c.sha().substr(0,8))
      key = commit.sha().substr(0, 8)
      c = {
        sha: commit.sha()
        message: commit.message()
        author: commit.author
        parents: parents
      }
      branches["master"].push key
      commits[key] = c
    history.on 'end', (r) ->
      console.log JSON.stringify({commits: commits, branches: branches})
    history.start()
]
```

[어제 정리한 Async.js](/holiday-project-day-10)를 이용해 콜백 중첩을 간소화했는데도, 여전히 계속 반복해 겹치는 콜백 스타일 코딩이 아직 적응되지 않는다.

이렇게 커밋간의 차이점(diff) 크기를 수치화하기 위한 데이터를 작성하다가, 콜백 스타일 코딩이 답답해서, 평소 익숙한 [루비 코드로도 재작성](https://github.com/hatemogi/karma-practice/blob/day-11/lib/repo_dump.rb)해봤는데, 일반적인 호출 스타일이 논리적 흐름에 자연스럽게 느껴졌다. 아니면, 단지 익숙하기 때문일 수도 있으니, 조금 더 써보며 적응해보려 한다.

이상으로 D3.js를 조금 익혀서 Git 저장소의 커밋 정보를 그래프로 그려보았다. 비록 멋지게 그리는 데에는 실패했지만, 그래도 D3.js와는 조금 친해진 느낌이다.

오늘은 여기까지.

[nodegit]: https://github.com/nodegit/nodegit



<script>
  var nodes = [];
  var links = [];

  var w = 700, h = 400;

  var chart = d3.select("svg.d3")
    .attr("width", w)
    .attr("height", h);

  var force = d3.layout.force()
    .distance(1)
    .nodes(nodes)
    .links(links)
    .size([w, h]);

  function circle_click() {
    data = d3.select(this.parentNode).datum();
    a = "<a href='https://github.com/nodegit/nodegit/commit/" + data.sha + "'> GitHub 링크: " + data.sha.substr(0, 8) + "</a>";
    $('.panel-heading').html(a);
    $('.panel-body').text(data.message);
    console.log(data);
  }

  d3.json('/js/holiday-project/day-11.json', function(r, json) {
    json['branches'].master.forEach(function(c, i) {
      nodes[i] = json.commits[c];
      nodes[i].index = i;
    });

    nodes.forEach(function(c, i) {
      c.parents.forEach(function(p) {
        links.push({source: c.index, target: json.commits[p].index});
      });
    });

    var link = chart.selectAll("line")
      .data(links)
      .enter().append("line");

    var node = chart.selectAll("g")
      .data(nodes)
      .enter().append("g")
      .attr("r", function(d) { return Math.min(d.change_level, 10); })
      .call(force.drag);

    node.append("circle")
      .attr("r", function(d) { return Math.min(d.change_level, 10); })
      .on('click', circle_click);

    // node.append("text").text(function(d) { return d.index; }).attr("dy", ".35em");

    force.on("tick", function() {
      link.attr("x1", function(d) { return d.source.x; })
          .attr("y1", function(d) { return d.source.y; })
          .attr("x2", function(d) { return d.target.x; })
          .attr("y2", function(d) { return d.target.y; });
      node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
    });
  });

  $('.show-button').on('click', function() {
    $(this.parentNode).hide();
    $('.graph-section').removeClass('hide').show(function() {
      force.start();
    });
  });

  $('.resume-button').on('click', function() {
    force.alpha(.99);
  });
</script>
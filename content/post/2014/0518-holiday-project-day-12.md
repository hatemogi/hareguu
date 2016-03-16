---
title: '프로젝트 12일: 그래프 그리기 도전 #2'
kind: article
date: 2014-05-18
layout: holiday-project-d3js
url: holiday-project-day-12
image: http://hatemogi.com/img/holiday-project/day-12-graph-2.png
description: '어제 D3.js의 Force Layout을 써서, Git 커밋 그래프를 그려봤다. 아무 생각 없이, 전체 커밋을 넣어서 실행해 봤더니, 마구 엉킨 그래프가 보였다. 오늘은 이 그래프의 모양을 어떻게 조금 아름답게 꾸밀 수 있을까를 고민했다.'
---

[어제] D3.js의 [Force Layout]을 써서, Git 커밋 그래프를 그려봤다. 아무 생각 없이, 전체 커밋을 넣어서 실행해 봤더니, 마구 엉킨 그래프가 보였다. 오늘은 이 그래프의 모양을 어떻게 조금 아름답게 꾸밀 수 있을까를 고민했다.

우선, 이제 와 고백하건대, 그래프에 대해 잘 모르겠다.

> 그래프? 그거 노드와 엣지가 있는 자료구조 아니던가?

이 정도가 내가 알고 있는 수준 전부이다. Git의 커밋 그래프는 일종의 [DAG]인데, Directed-Acyclic-Graph의 약자이며, 방향이 있는 그래프인데, 방향의 순환참조가 없는 그래프를 말한다. Git의 내부 구조는 이 DAG가 핵심이다. [컴퓨터과학자를 위한 Git]이라는 문서에 아주 친절히 설명이 돼 있다.

그래프도 모르는데, DAG는 어찌 안담? 오래전 학창시절에, 알고리즘 시간에 그래프 순회 알고리즘을 배운 것도 같은데, 이런 그래프도 배웠던가? 혹시나 싶어서, 먼지 폴폴 쌓여있는 학창시절의 알고리즘 교과서를 뒤져봤다. 최단 거리를 찾는 다익스트라 알고리즘을 설명하는 부분에 그래프 순회가 나오기는 했으나, DAG라고 자세히 나오지는 않았다. 휴, 그나마 다행이긴 하나, 만약 배웠다 하더라도, 내가 기억하고 있으리라는 보장은 없다. 그 다익스트라 알고리즘도 지금 짜보라면, 당황할 것이 뻔하지 않은가?!

이번 프로젝트를 진행하면서 느끼는 것 중 하나는, "내가 일하면서 컴퓨터 과학에 관해 이론적으로 공부해 볼 만한 일은 별로 없었다"는 것이었다. 나름 오랫동안 인터넷 포털에서 개발자로 근무하며 한 일이라고는, 대규모 사용자가 웹서비스 접근해오면 어떻게 대응해야 하는지 트래픽 대응, 분산처리, 캐시 처리 잘하기, DB에 넣고 빼고 읽은 거 웹에 뿌리는 일처럼 매번 하는 일들을 매년 반복해 왔던 것 같다.

그냥 환경이 계속 바뀔 뿐이다. 웹이 AJAX낀 웹이 되고, 스트럿츠로 개발하다가 스프링으로 개발하고, 그러다가 레일스로 하고, 아니면 아이폰 개발하고, 뭔가 개발하는 도구나 플랫폼이 바뀔 뿐, 새로운 이론을 배운다거나 과거에 배운 것이라도 활용해 보는 일은 별로 없는 것이다. 경력 "기간"은 쌓이는데, 진짜 경력이 쌓이는 것인지는 알 수 없다. 알고리즘이랍시고 적용해 본 것은, 캘린더 서비스 개발할 때, 일정 그래프가 겹치지 않도록 배치하는 것 정도가 다였다. 나머지는 뭐 맨...

지질한 푸념은 여기까지로 줄이고, 그럼 이제 어떻게 할 것인가? 이제부터라도 공부하면 되는 것 아닌가? 그래프 드로잉이 지금의 주요 관심사니, 관련 책을 찾아봤다. "Graph Drawing: Algorithms for the Visualization of Graphs"라는 책이 눈에 띄었는데, 가격이 너무 부담된다. 아쉬운 대로, 온라인 책을 찾았다.

> [Handbook of Graph Drawing and Visualization](http://cs.brown.edu/~rt/gdhandbook/)

각 챕터를 PDF 파일로 내려받아 볼 수 있다. 그래, 위키피디아랑 온라인 문서들부터라도 틈틈히 읽도록 하자. 문제는 이 그래프 드로잉이라는 주제에 깊이 빠져들면, 30일은커녕 3개월이 되더라도 별다른 결과가 나오지는 않을 것 같다는 것. 아아~

그래프에 대해 공부도 해야겠지만, 프로젝트 진행을 위해 모르면 모르는 대로 진행은 해봐야겠다 싶어서, 단순 무식하게 생각해 낸 아이디어는,

> Force Layout을 이용하되, 중력 기준점을 왼쪽에 두고, 오른쪽에 고정좌표로 최종 헤드의 커밋을 두면 나름 일직선으로 그럴싸하게 보이지 않을까?

이 생각을 바탕으로 어제의 그래프를 다시 그려봤다. 일단, 커밋을 최종 30개만 가져다가, 그린 그래프는 아래와 같다.

[DAG]: https://en.wikipedia.org/wiki/Directed_acyclic_graph
[어제]: /holiday-project-day-11/
[D3.js]: http://d3js.org
[Force Layout]: https://github.com/mbostock/d3/wiki/Force-Layout
[컴퓨터과학자를 위한 Git]: http://eagain.net/articles/git-for-computer-scientists/

<style>
  circle {
    stroke: black;
    stroke-width: 1.5px;
    fill: steelblue;
    cursor: pointer;
  }
  circle.center {
    stroke: red;
    stroke-width: 1px;
    fill: black;
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

<div class="graph-section">
  <h3>어떤 Git 저장소의 master 브랜치에 있는 최근 커밋 30개</h3>
  <svg class="d3"></svg>
  <div class="panel panel-info">
    <div class="panel-heading">
      원을 클릭해 보세요.
    </div>
    <div class="panel-body">
      커밋메시지가 보일 거에요.
    </div>
  </div>
</div>

이론적 바탕없이 무작정 달려든 데에 대한 당연한 결과겠지만, 역시 실패. 방향은 대충 원하는 그림으로 나왔으나, 너무 고정 좌표 쪽으로 노드들이 당겨진 나머지, 위나 아래에 그려졌어야 하는 마지막에서 두 번째 노드가 일직선 상에 위치해버렸다. (맨 오른쪽에서 두 번째 노드를 살짝 드래그해보면 무슨 얘긴지 안다.)

이쯤에서 그만 다시 graphviz에게 모든 걸 맡겨볼까? 내일은 이 난국을 어떻게 헤쳐나갈지부터 다시 고민해야겠다.

오늘은 (씁쓸한 마음으로) 여기까지.

<script>
(function() {
  var chart, circle_click, force, gravity_x, h, links, margin_x, nodes, w, x, y;

  nodes = [];

  links = [];

  w = 700;

  h = 200;

  chart = d3.select("svg.d3").attr("width", w).attr("height", h);

  force = d3.layout.force().distance(1).charge(-50).chargeDistance(30).nodes(nodes).links(links).size([w, h]);

  circle_click = function() {
    var a, data;
    data = d3.select(this).datum();
    a = "<a href='https://github.com/nodegit/nodegit/commit/\#{data.sha}'>GitHub 링크: \#{data.sha.substr(0, 8)}</a>";
    $('.panel-heading').html(a);
    $('.panel-body').text(data.message);
    return console.log(data);
  };

  margin_x = 50;

  gravity_x = w / 2;

  x = function(x) {
    return x - w / 2 + margin_x;
  };

  y = function(y) {
    return y;
  };

  d3.json('/js/holiday-project/day-11.json', function(r, json) {
    var link, node;
    json['branches'].master.slice(0, 30).forEach(function(c, i) {
      nodes[i] = json.commits[c];
      return nodes[i].index = i;
    });
    nodes.forEach(function(c, i) {
      return c.parents.forEach(function(p) {
        if (nodes[json.commits[p].index]) {
          return links.push({
            source: c.index,
            target: json.commits[p].index
          });
        }
      });
    });
    link = chart.selectAll("line").data(links).enter().append("line");
    node = chart.selectAll("circle").data(nodes).enter().append("circle").attr("r", function(d) {
      return Math.min(d.change_level, 10);
    }).call(force.drag).on("click", circle_click);
    force.on("tick", function() {
      nodes[0].x = w * 1.4 - margin_x;
      nodes[0].y = h / 2;
      nodes[0].fixed = true;
      link.attr("x1", function(d) {
        return x(d.source.x);
      }).attr("y1", function(d) {
        return y(d.source.y);
      }).attr("x2", function(d) {
        return x(d.target.x);
      }).attr("y2", function(d) {
        return y(d.target.y);
      });
      return node.attr("cx", function(d) {
        return x(d.x);
      }).attr("cy", function(d) {
        return y(d.y);
      });
    });
    return force.start();
  });

}).call(this);
</script>
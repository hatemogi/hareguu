---
title: '프로젝트 13일: 컨테이너 기반 가상화 프레임워크 - Docker'
kind: article
date: 2014-05-19
layout: holiday-project
image: https://www.docker.io/static/img/homepage-docker-logo.png
url: holiday-project-day-13/
description: 'Docker는 가상화 프레임워크인데, 컨테이너 기반 가상화를 사용한다. 일반적인 가상화 방식은 호스트 OS와 게스트 OS가 따로 있고, 게스트 OS부터 분리해 독립된 운영환경을 제공하지만, Docker는 이와 달리, 컨테이너 기반 가상화는 호스트 OS를 그대로 공유하고, 유저 스페이스에서 가상화를 제공한다.'
---

[어제](/holiday-project-day-12/)는 그래프 그리기의 벽을 만났다. 그래프를 본격적으로 공부하자니, 목표로 한 30일 내에는 결과가 나올 것 같지 않다. 한가지 소득이 있다면, 새로 공부할 거리를 장전했다는 것. 이번 휴가가 끝나도 틈틈이 공부할 주제다.

혹시나 [Coursera](https://www.coursera.org/)에 관련 수업이 있지는 않나 해서 찾아봤는데, 8월과 10월에 그래프 그리는 것과 연관이 있어 보이는 수업이 있어서 바로 등록했다.

> * 8월, [Interactive Computer Graphics](https://www.coursera.org/course/interactivegraphics)
> * 10월, [Social Network Analysis](https://www.coursera.org/course/sna)

어떤 주제에 대해 처음으로 알아볼 때 가장 어려운 점은 **어디서 무엇부터 공부하는지를 파악하는 것**이 아닐까? 무엇을 모르는 것인지조차 잘 모르는 상태다. 다행인 것은, 꼬인 끈이라도 하나 잡고 파고들다 보면 조금씩 시야가 드러난다. 그러길 바란다.

꼭 해야 하는 (업무) 프로젝트인 경우에는, 벽을 만나도 어떻게든 추진해야겠지만, 개인 (취미) 프로젝트인 경우 이런 벽을 만났다고 해서 느슨하게 뒤로 미룬다고 하면 바로 중단되기 쉽다. 안 그래도 의지가 부족한데, 좋은 핑곗거리가 생기는 것이다.

Docker 알아보기
----------------

그래프 그리기와는 전혀 다른 주제로, 오늘은 [Docker]를 설치해봤다. (이럴 때일수록 뭐라도 일단  진행해야 한다.)

[Docker]는 사실 이번 프로젝트가 아주 성공적으로 진행될 경우 필요한 컴포넌트다. 다시 말해, 당장은 필요없는 컴포넌트일 수 있는데, 예전부터 한번 시간이 되면 알아보고 싶었던 프레임워크다. 지금이 그런 "시간" 아닐까?

> Docker is a container based virtualization framework. Unlike traditional virtualization Docker is fast, lightweight and easy to use. Docker allows you to create containers holding all the dependencies for an application. Each container is kept isolated from any other, and nothing gets shared.

가장 괜찮아 보이는 설명을 발췌했다. Docker는 가상화 프레임워크인데, 컨테이너 기반 가상화를 사용한다. 일반적인 가상화 방식은 호스트 OS와 게스트 OS가 따로 있고, 게스트 OS부터 분리해 독립된 운영환경을 제공하지만, Docker는 이와 달리, 컨테이너 기반 가상화는 호스트 OS를 그대로 공유하고, 유저 스페이스에서 가상화를 제공한다.

![](/img/holiday-project/docker_vm.jpg)

_그림: 일반적인 가상화와 컨테이너 비교 (<https://www.docker.io/the_whole_story/>에서 가져온 이미지)_

[LXC]나 [BSD Jail]을 안다면 더 설명이 필요없는 내용이다. (실제로도 [Docker]는 [LXC]를 이용한다.)

뭐가 좋을까? 일반적인 가상화에서 그렇듯, 각각의 애플리케이션이 완전히 독립된(dedicated) 영역에서 CPU, 네트워크, 디스크 자원을 활용할 수 있지만, 그보다 훨씬 가볍다. 마치, 그냥 리눅스 서버에 프로세스 여러 개를 띄운 것 같이 가벼운데, 그 애플리케이션 간의 간섭이 없는 것이다. 다른 점은, 호스트 OS와 같은 OS 환경만을 제공한다는 것이다. 이 때문에 가볍다는 장점이 되는 것이기도 하다.

또 하나 언급할 만한 점은, 이미 (일반적인) 가상화 장비를 받은 상황에서, 다시 [Docker]를 이용해서 또 자원을 격리 배분할 수 있다. [AWS EC2]나 [Google Compute Engine]에서 리눅스 VM 한 개 받아서, 그 안에 다시 Docker를 이용, 또다시 여러 개의 가상환경을 쪼개서 만들 수 있다. 그 컨테이너들을 내 애플리케이션들이 다 사용하든, 아니면 내 서비스를 사용하는 사용자에게 하나씩 나눠서 제공하든 그것은 자유다.

> Docker containers can encapsulate any payload, and will run consistently on and between virtually any server. The same container that a developer builds and tests on a laptop will run at scale, in production, on VMs, bare-metal servers, OpenStack clusters, public instances, or combinations of the above.

또 다른 Docker의 특징 중 하나는, 클라우드 환경이든 내 로컬환경이든, 준비해둔 그 가벼운 ```컨테이너```를 어디서나 실행할 수 있다는 점이다. 개발할 때 쓰는 맥북 프로에 Docker 컨테이너를 준비해두고, 그 컨테이너를 그대로 퍼블릭 클라우드에 올려서 서비스할 수도 있다는 얘기다.

오늘 Docker에 대해서 알아보기 전에는, 로컬 [VirtualBox]에 우분투 깔고, 그 안에서 docker를 설치하고 무언가 연습을 해볼 것이라 예상했다. 한번 쭉 해보고 [Vagrant]로 개발/테스트 환경을 구축하게 정리할 생각을 했다. 그런데, 조금 더 알아보니 그럴 필요가 없는 것 같다. Docker가 그 관련한 모든 일을 너무 편하게 다 처리해준다. 내가 개발/테스트 환경을 위해 할 일은 훨씬 적을 것 같다.

Docker 설치해보기
-----------------

우선 한번 따라 해 설치해봤다. 실제 직접 설치해보려거든 이하 내용은 참고만 하고, 원래 사이트의 문서를 읽고 진행하자. 이런 내용은 버전에 따라 쉽게 바뀌고, 이런 블로그 글은 쉽게 쓸모없어지니까 말이다. [Installing Docker on Mac OS X](http://docs.docker.io/installation/mac/)라고, 친절하게 Mac OS X에 설치하는 방법이 따로 잘 정리돼 있다. (다른 운영체제나 퍼블릭 클라우드 환경에 설치하는 방법도 아주 잘 나와 있다.)

심지어 그 설치 과정도 [Homebrew]로 몇 단계 만에 끝난다. 로컬에 [VirtualBox]와 [Homebrew]가 설치된 상황에서 입력한 커맨드는 아래가 전부다.

```bash
brew install boot2docker
brew install docker
boot2docker init
boot2docker up

boot2docker ssh
# User: docker
# Pwd:  tcuser
```

마지막 커맨드로, Docker가 만든 기본적인 리눅스 박스에 ssh접근할 수 있다. 그 리눅스 박스에서 아래의 커맨드를 실행해봤다.

```bash
sudo docker info
sudo docker pull ubuntu
```

이 박스에서부터 우분투 환경을 준비하든 CentOS를 준비하든, 각종 컨테이너를 만들어서 실행하고 관리할 수 있는 것. 좀 더 활용해보고,  "[Amazon EC2에 Docker 설치하기](http://docs.docker.io/installation/amazon/#amazon-quickstart)" 같은 문서를 읽어보며 퍼블릭 클라우드에 올려보면 되겠다.

문서 곳곳에 "Docker를 비록 아직 프로덕션 레벨로는 쓰지 말라"는 경고가 번듯이 적혀있지만, [곧 1.0 배포를 앞둔 단계](http://blog.docker.io/2014/05/docker-0-11-release-candidate-for-1-0/)고, 약간의 모험을 감당할 수 있는 수준이라면 문제없이 써볼 수 있겠다. 지금 내 개발 프로젝트 같은 경우 걱정 없이 쓸 수 있는 상황이다.

1~2년 전에 LXC를 직접 써서, 컨테이너 가상화를 해보려고 했는데, 네트워크 설정도 어렵고, 디스크 일일이 마운트 하기도 복잡하게 느껴져서, 결국 다음 기회로 미뤘던 기억이 있다. 그 작업의 기억에 비하면, [Docker]가 제공해주는 편리함은 **신세계**다.

이번 프로젝트가 아니더라도, 유용하게 활용한 만한 일이 많을 것이라 예상한다.

오늘은 여기까지.


[Docker]: https://www.docker.io/
[LXC]: https://linuxcontainers.org/
[BSD Jail]: https://en.wikipedia.org/wiki/FreeBSD_jail
[AWS EC2]: http://aws.amazon.com/ko/ec2/
[Google Compute Engine]: https://cloud.google.com/products/compute-engine/
[VirtualBox]: https://www.virtualbox.org/
[Vagrant]: http://www.vagrantup.com/
[Homebrew]: http://brew.sh/index_ko.html

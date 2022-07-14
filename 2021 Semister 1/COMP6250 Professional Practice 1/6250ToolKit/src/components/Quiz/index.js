import React from "react";
import styles from "./index.module.less";
import {Checkbox, Divider, Steps, Radio, Button} from "antd";
import {Link, withRouter} from "react-router-dom";

const questions = [
  {
    topic: "Being a Systems Thinker",
    description: [
      "System engineering is a trend skill for engineers to solve complex problems in the future. A complex system often has more than itself, and there are many other systems that directly or indirectly affect each other. Being able to analyze which systems will be affected and find out how they affect each other is a very useful skill. At the same time, problems may occur from multiple angles, different stages, and different reasons. It is also very important to fully consider these possible issues.",
      "Suppose you are helping the government build a system to prevent and control drunk driving to reduce the harm it brings."
    ],
    quizzes: [
      {
        description: "Which of the following systems may be affected?",
        options: [
          {
            key: 1,
            text: "Transportation system"
          },
          {
            key: 2,
            text: "Police system"
          },
          {
            key: 3,
            text: "Medical system"
          },
          {
            key: 4,
            text: "Government"
          }
        ],
        answer: [1, 2, 3, 4],
        feedback: [
          {
            key: 1,
            text: "This system is about drunk driving, so the transportation system is most directly affected."
          },
          {
            key: 2,
            text: "The investigation and handling of drunk driving normally requires the work of the police department, and changes aimed at drinking and driving will also affect the work of the police department."
          },
          {
            key: 3,
            text: "Reducing drunk driving can reduce injuries and deaths caused by traffic accidents, which in turn affects the medical system."
          },
          {
            key: 4,
            text: "The government can formulate policies to reduce drunk driving. The severity of drunk driving also will in turn affect the formulation of government policies."
          }
        ]
      },
      {
        description: "For a system thinker, which of the following habits do you think are good?",
        options: [
          {
            key: 1,
            text: "Create the best solution possible using available resources."
          },
          {
            key: 2,
            text: "Know that some problems cannot be solved by an individual."
          },
          {
            key: 3,
            text: "Learn from history and contribute to future knowledge."
          }
        ],
        answer: [1, 2, 3],
        feedback: [
          {
            key: 1,
            text: "A good engineer will create the best solution possible using available resources, though there are lots of contentious words in that statement. Best is a value proposition, you have to define what best is, often this is done through requirements or specifications on a job being able to create.",
          },
          {
            key: 2,
            text: "The thing that we normally need engineers to be able to deal with is to work on problems that can't be solved at an individual level. It's going to be done as a group, and it's going to be done. And because you're part of that group, it's going to be better than something that you could do by yourself."
          },
          {
            key: 3,
            text: "Being able to learn from what mistakes happened and what others had learned in the past, or maybe have a look at it in a different way is really important. The knowledge learned from the past can be applied to new problems."
          }
        ]
      },
      {
        description: "To finish this task well, which skill(s) do you think you need to have?",
        options: [
          {
            key: 1,
            text: "Basic professional knowledge."
          },
          {
            key: 2,
            text: "Social skills."
          },
          {
            key: 3,
            text: "Teamwork ability."
          },
        ],
        answer: [1, 2, 3],
        feedback: [
          {
            key: 1,
            text: "You need to have basic electrical engineering knowledge to solve the technical problems in the project."
          },
          {
            key: 2,
            text: "The social skills are also crucial to finish the project with the government staff."
          },
          {
            key: 3,
            text: "You need to have teamwork ability to cooperate with your colleague and team leader. It is important that we need to have both skills above to finish the project successfully, not just the professional knowledge."
          }
        ]
      }
    ]
  },
  {
    topic: "Mapping the Ecosystem",
    description: [
      "Stakeholder analysis is crucial to the engineers for different projects which involve different stakeholders. We need to analyze the interest of every stakeholder and their relevance to the project and make an ecosystem map when necessary.",
      "Suppose you are working on a project about mining in one area, what stakeholder(s) you need to consider about?"
    ],
    quizzes: [
      {
        description: "What stakeholder(s) you need to consider about?",
        options: [
          {
            key: 1,
            text: "The government."
          },
          {
            key: 2,
            text: "The local residents."
          },
          {
            key: 3,
            text: "The mining contractors."
          }
        ],
        answer: [1, 2, 3],
        feedback: [
          {
            key: 1,
            text: "You need to consider the government for the stakeholder analysis because the government can decide whether the project can continue to take place considering the budget and the problems it will cause."
          },
          {
            key: 2,
            text: "You need to consider the local residents’ thoughts about the project, such as the pollution problem and the noise problem."
          },
          {
            key: 3,
            text: "The contractors are also crucial in this project because they will provide the transport service and some other services. You need to consider their interests and thoughts."
          }
        ]
      },
    ]
  },
  {
    topic: "Being Ethical in Research",
    description: [
      "Sometimes we may encounter situations where decision-making is contrary to morality. As a responsible profession at this time, we should have the ability to distinguish right from wrong and make changes.",
      "Suppose you are an engineer. Your boss Tom and you need to finish a project together, but this project involves the environment problem, because you know that the current technology will certainly bring some pollution to the environment. The boss also knew that the technology would pollute the environment, but the boss only cared about the benefits of the project and insisted that the project go ahead.",
      "Do you respect moral principles or violate your conscience?"
    ],
    quizzes: [
      {
        description: "What should you do?",
        options: [
          {
            key: 1,
            text: "Follow the boss to violate ethics to finish projects."
          },
          {
            key: 2,
            text: "Stick to the principles of the engineer."
          },
          {
            key: 3,
            text: "Negotiate with your boss whether to complete the project ethically."
          }
        ],
        answer: [2],
        feedback: [
          {
            key: 1,
            text: "It may have some impacts. You may be denounced by the media and environmental groups, and you may have to spend more money to make up for it."
          },
          {
            key: 2,
            text: "You may lose your occupation because of this decision. The boss may think you are hurting his interests. But you will be respected by many people for sticking to this principle."
          },
          {
            key: 3,
            text: "There are two situations that can happen. First case, you make an agreement with your boss that you continue to develop the technology to reduce the pollution to a minimum and tell the media and government the advantages and disadvantages of this project. In the second case, the boss overrules your opinion."
          }
        ]
      },
      {
        description: "There are numerous ways of analysing ethical issues, in Australia, one that is well published is the \"Doing Ethics Technique (DET)\". DET uses a series of questions in a particular order to explore the issues the professional is facing and uses the answers to help develop satisfactory outcomes. Which of the following order of these questions is right?",
        options: [
          {
            key: 1,
            text: "1. What is going on? 2. Who is affected? 3. What are the issues? 4. What are the facts? This leads to: 5. What can be done about it? 6. What options are there? 7. What are the ethical issues and implications? 8. Which option is best-and why?"
          },
          {
            key: 2,
            text: "1. Who is affected? 2.What is going on? 3. What are the issues? 4. What are the facts? This leads to: 5. What can be done about it? 6.What are the ethical issues and implications? 7. What options are there? 8. Which option is best-and why?"
          },
          {
            key: 3,
            text: "1. What is going on? 2.What are the facts? 3. What are the issues? 4. Who is affected? This leads to: 5. What can be done about it? 6.What are the ethical issues and implications? 7. What options are there? 8. Which option is best-and why?"
          },
          {
            key: 4,
            text: "1. What is going on? 2.What are the facts? 3. What are the issues? 4. Who is affected? This leads to: 5. What are the ethical issues and implications? 6.What can be done about it? 7. What options are there? 8. Which option is best-and why?"
          }
        ],
        answer: [4],
        feedback: [
          {
            key: 1,
            text: "The order is: 1. What is going on? 2.What are the facts? 3. What are the issues? 4. Who is affected? This leads to: 5. What are the ethical issues and implications? 6.What can be done about it? 7. What options are there? 8. Which option is best-and why?"
          },
          {
            key: 2,
            text: "The order is: 1. What is going on? 2.What are the facts? 3. What are the issues? 4. Who is affected? This leads to: 5. What are the ethical issues and implications? 6.What can be done about it? 7. What options are there? 8. Which option is best-and why?"
          },
          {
            key: 3,
            text: "The order is: 1. What is going on? 2.What are the facts? 3. What are the issues? 4. Who is affected? This leads to: 5. What are the ethical issues and implications? 6.What can be done about it? 7. What options are there? 8. Which option is best-and why?"
          },
          {
            key: 4,
            text: "The order is: 1. What is going on? 2.What are the facts? 3. What are the issues? 4. Who is affected? This leads to: 5. What are the ethical issues and implications? 6.What can be done about it? 7. What options are there? 8. Which option is best-and why?"
          }
        ]
      }
    ]
  },
  {
    topic: "Being Ethical in Practice",
    description: [
      "In engineering practice ,we always face some ethical problems, especially when we work on an actual project, we need to make a suitable decision between the deadline and the quality of the project result.",
      "Suppose you are a newcomer in a company, your boss asks you to develop a project as a leader. It is almost the deadline, the project operation effect is not ideal. However, the deadline is so important."
    ],
    quizzes: [
      {
        description: "What should you do?",
        options: [
          {
            key: 1,
            text: "Make the project work best regardless of the deadline."
          },
          {
            key: 2,
            text: "The deadline is important, just upload your project to your boss.",
          },
          {
            key: 3,
            text: "Explain to your boss and apply for more time to improve your project."
          }
        ],
        answer: [3],
        feedback: [
          {
            key: 1,
            text: "Deadlines are important when you work in a company. If you do not follow the deadline, you may be punished. You did not complete the task in accordance with the agreed time, which violates the spirit of the contract."
          },
          {
            key: 2,
            text: "The project performance is a projection of actual application. Bad performance may cause serious consequences, which is irresponsible."
          },
          {
            key: 3,
            text: "It is a good way to tell your boss the truth. You take the responsibility, you may be punished for it, but if you can get the permission, you can work on your project with a new deadline."
          }
        ]
      },
      {
        description: "Along the development, You found a design flaw, this flaw may cause security problems under certain circumstances. But the conditions that trigger this problem are very harsh, and the deadline is coming soon. What would you do?",
        options: [
          {
            key: 1,
            text: "Report the problem to the boss and insist on solving it."
          },
          {
            key: 2,
            text: "Conceal the problem because the probability of triggering this problem is very small."
          }
        ],
        answer: [1],
        feedback: [
          {
            key: 1,
            text: "You practiced morality. When there is a problem in the project, we should bring it up and solve it in order to eliminate hidden dangers. Although the probability of a problem is very small, there may still be a problem, and it will cause harm to others."
          },
          {
            key: 2,
            text: "As a responsible profession, we should always be ethical. Although the probability of a problem is very small, there may still be a problem, and it will cause harm to others. When there is a problem in the project, we should bring it up and solve it to eliminate hidden dangers."
          }
        ]
      }
    ]
  },
  // {
  //   topic: "Being Responsible in Innovation",
  //   description: [
  //     "In social progress and technological development, innovation and invention are indispensable. However, innovation does not always bring benefits. Bad innovation and incorrect application of new technologies may cause damage to the environment and society, and even cause disasters."
  //   ]
  // },
  {
    topic: "Having a Deep Conversation",
    description: [
      "Communication at work is indispensable, so how to communicate deeply and effectively is a very important skill. ",
      "Suppose you are working on a job with Amy, but you have different ideas about a problem, and you both think you are right."
    ],
    quizzes: [
      {
        description: "What should you do?",
        options: [
          {
            key: 1,
            text: "Wrangle with her because you are correct.",
          },
          {
            key: 2,
            text: "Ask her humbly why she thinks so.",
          },
          {
            key: 3,
            text: "Give up your own opinion, because she is more experienced.",
          }
        ],
        feedback: [
          {
            key: 1,
            text: "Wrangle doesn't seem to be a good idea. " +
              "It makes her feel disrespected. " +
              "Also, maybe Amy's idea is also reasonable, and it might be better to understand " +
              "why she has such a view, because her ideas may also be inspiring for you."
          },
          {
            key: 2,
            text: "To know her thoughts is a good choice. " +
              "Leaving out the disputes and understanding why she thinks this way " +
              "may help to refine your own idea. " +
              "And, you have also shown respect for her."
          },
          {
            key: 3,
            text: "Deep communication is based on respect for everyone. " +
              "Don't give up your opinion easily, maybe your opinion is reasonable. " +
              "By communicating with each other and exchanging ideas, " +
              "both of your ideas can be supplemented, " +
              "and you may be able to get better results."
          }
        ],
        answer: [2],
      },
      {
        description: "To have a deep conversation, what do you think are correct below?",
        options: [
          {
            key: 1,
            text: "Be a good listener."
          },
          {
            key: 2,
            text: "Always respect others."
          },
          {
            key: 3,
            text: "Persevere in persuading others."
          },
          {
            key: 4,
            text: "Give others an opportunity to express their opinions and understand what the other person thinks."
          }
        ],
        answer: [1, 2, 4],
        feedback: [
          {
            key: 1,
            text: "Being a good listener allows you to understand the ideas of others, and learn knowledge from others for your own use."
          },
          {
            key: 2,
            text: "We should always respect others, even if they have different opinions from ourselves. Maintaining respect is conducive to the smooth progress of the conversation."
          },
          {
            key: 3,
            text: "Persevering in persuading others does not seem to be a good idea. Different people may have different opinions, and these opinions may be reasonable. We should fully understand others' thoughts and absorb them critically."
          },
          {
            key: 4,
            text: "We should give others opportunities to express their opinions so we can have the opportunities to learn new things from other people's ideas, improve our own ideas, and promote better results."
          }
        ]
      },
    ]
  },
  {
    topic: "Working in a team",
    description: [
      "Teamwork is the leading trend in any industry today. How to build a good atmosphere among team members and how to bring cooperation into full play into the work should be considered by all the engineers as preparation for the future career.",
      "Suppose you are on a team made by people with different cultural backgrounds, and you have never met each other before. This is the first meeting for the group project, what should you do?"
    ],
    quizzes: [
      {
        description: "Suppose you decided to start the topic first. Which process do you think would be better?",
        options: [
          {
            key: 1,
            text: "task division – self-introduction – responsibility confirmation – ice broken activity",
          },
          {
            key: 2,
            text: "self-introduction – ice breaking activity – task division – responsibility confirmation",
          },
          {
            key: 3,
            text: "self-introduction – ice breaking activity – responsibility confirmation – task division",
          }
        ],
        feedback: [
          {
            key: 1,
            text: "Self-introduction will be a good start when you are facing people you are not familiar with. It will introduce the ice-broken topic like “how about you guys”. After everyone introduces themselves, you may know what they are good at or interested in. By this time, you can start arranging the tasks for each member and don’t forget to make confirmations with each other before the end of the first meeting. As a team leader, you should make sure every member totally understands what they are required at the beginning so that you can avoid wasting too much time in the future.",
          },
          {
            key: 2,
            text: "Self-introduction will be a good start when you are facing people you are not familiar with. It will introduce the ice-broken topic like “how about you guys”. After everyone introduces themselves, you may know what they are good at or interested in. By this time, you can start arranging the tasks for each member and don’t forget to make confirmations with each other before the end of the first meeting. As a team leader, you should make sure every member totally understands what they are required at the beginning so that you can avoid wasting too much time in the future.",
          },
          {
            key: 3,
            text: "Self-introduction will be a good start when you are facing people you are not familiar with. It will introduce the ice-broken topic like “how about you guys”. After everyone introduces themselves, you may know what they are good at or interested in. By this time, you can start arranging the tasks for each member and don’t forget to make confirmations with each other before the end of the first meeting. As a team leader, you should make sure every member totally understands what they are required at the beginning so that you can avoid wasting too much time in the future.",
          }
        ],
        answer: [2],
      },
      {
        description: "If you are arranged to search for some cases for the project with another person, what should you do before ending the meeting?",
        options: [
          {
            key: 1,
            text: "Ask for further contact with that person."
          },
          {
            key: 2,
            text: "Re-check which topic that you are required with the team leader."
          },
          {
            key: 3,
            text: "Create a shared file for the group if not."
          },
          {
            key: 4,
            text: "All the above."
          }
        ],
        answer: [4],
        feedback: [
          {
            key: 1,
            text: "Since you are working with others, to avoid finding the same resources, you have to arrange your work again later. Also, in case you misunderstand the purpose, re-check with the leader is necessary. Plus, you can ask which is the key part for the project needs. For example, if you are required to find the failure cases of remote working, you can ask whether the reasons for the failure or the other points that should be enhanced. At last, a shared document will become a good reference for you to summarize your resources. And it will become convenient for the future meeting."
          },
          {
            key: 2,
            text: "Since you are working with others, to avoid finding the same resources, you have to arrange your work again later. Also, in case you misunderstand the purpose, re-check with the leader is necessary. Plus, you can ask which is the key part for the project needs. For example, if you are required to find the failure cases of remote working, you can ask whether the reasons for the failure or the other points that should be enhanced. At last, a shared document will become a good reference for you to summarize your resources. And it will become convenient for the future meeting."
          },
          {
            key: 3,
            text: "Since you are working with others, to avoid finding the same resources, you have to arrange your work again later. Also, in case you misunderstand the purpose, re-check with the leader is necessary. Plus, you can ask which is the key part for the project needs. For example, if you are required to find the failure cases of remote working, you can ask whether the reasons for the failure or the other points that should be enhanced. At last, a shared document will become a good reference for you to summarize your resources. And it will become convenient for the future meeting."
          },
          {
            key: 4,
            text: "Since you are working with others, to avoid finding the same resources, you have to arrange your work again later. Also, in case you misunderstand the purpose, re-check with the leader is necessary. Plus, you can ask which is the key part for the project needs. For example, if you are required to find the failure cases of remote working, you can ask whether the reasons for the failure or the other points that should be enhanced. At last, a shared document will become a good reference for you to summarize your resources. And it will become convenient for the future meeting."
          },
        ]
      },
    ]
  },
];

class Quiz extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      currentChapter: 0,
      currentQuiz: 0,
      currentAnswer: null,
      confirmedAnswer: null,
      allDone: false,
    }
  }

  getSteps = () => {
    let steps = [];
    for (const i in questions) {
      steps.push(i+1)
    }
    return steps;
  };

  onAnswerChange = (e) => {
    console.log(e)
    const val = e.target ? e.target.value : e;
    console.log(val)
    this.setState({currentAnswer: val})
  }

  onAnswerConfirm = e => {
    this.setState({
      confirmedAnswer: this.state.currentAnswer
    })
  }

  onNextQuiz = e => {
    console.log("next")
    let {currentChapter, currentQuiz, allDone} = this.state;
    const chapter = questions[currentChapter];
    if (currentQuiz < chapter.quizzes.length - 1) {
      currentQuiz += 1;
    } else {
      currentChapter += 1;
      currentQuiz = 0;
    }
    if (currentChapter >= questions.length) {
      allDone = true;
    }

    this.setState({
      currentChapter,
      currentQuiz,
      currentAnswer: null,
      confirmedAnswer: null,
      allDone
    })
  }

  onTryAgainClick = () => {
    this.setState({
      currentChapter: 0,
      currentQuiz: 0,
      currentAnswer: null,
      confirmedAnswer: null,
      allDone: false
    });
  }

  renderChapter = () => {
    const {currentChapter} = this.state;
    const chapterInfo = questions[currentChapter];
    let descriptions = [];
    let counter = 0;
    for (const description of chapterInfo.description) {
      descriptions.push(<p key={counter++}>{description}</p>);
    }
    return (
      <div className={styles.chapter}>
        <h1>Chapter {currentChapter + 1}: {chapterInfo.topic}</h1>
        <div className={styles.descriptions}>
          {descriptions}
        </div>
      </div>
    );
  };

  renderQuiz = () => {
    const {currentChapter, currentQuiz, currentAnswer} = this.state;
    const quizzes = questions[currentChapter].quizzes;
    const quiz = quizzes[currentQuiz];

    const isMulti = quiz.answer.length !== 1;
    return (
      <div className={styles.quiz}>
        <p className={styles.quizDescription}>
          <b>Question {currentQuiz + 1}: </b>
          {quiz.description}
          {isMulti ? " (Please select multiple options)" : null}
        </p>

        {
          isMulti ?
            <Checkbox.Group
              value={currentAnswer ? currentAnswer : []}
              onChange={this.onAnswerChange}
            >
              {this.getCheckBoxGroup(quiz)}
            </Checkbox.Group>:
            <Radio.Group
              value={currentAnswer}
              onChange={this.onAnswerChange}
            >
              {this.getRadioGroup(quiz)}
            </Radio.Group>
        }
      </div>
    );
  }

  getCheckBoxGroup = (quiz) => {
    const group = [];
    for (const option of quiz.options) {
      group.push(
        <div className={styles.checkbox} key={option.key}>
          <Checkbox key={option.key} value={option.key}>{option.text}</Checkbox>
        </div>
      );
    }
    return group;
  }


  getRadioGroup = quiz => {
    const group = [];
    for (const option of quiz.options) {
      group.push(
        <div className={styles.checkbox} key={option.key}>
          <Radio key={option.key} value={option.key}>{option.text}</Radio>
        </div>
      );
    }
    return group;
  }

  findFeedBack = (ans, quiz) => {
    for (const feedback of quiz.feedback) {
      if (feedback.key === ans) {
        return feedback
      }
    }
    return null;
  }

  verifyAnswer = () => {
    let {confirmedAnswer, currentChapter, currentQuiz} = this.state;
    const quizzes = questions[currentChapter].quizzes;
    const quiz = quizzes[currentQuiz];
    const isMulti = quiz.answer.length !== 1;
    if (!isMulti) {
      confirmedAnswer = [confirmedAnswer];
    }
    confirmedAnswer = confirmedAnswer.sort();
    const answer = quiz.answer.sort();

    let flag = 0;
    if (confirmedAnswer.length !== answer.length) {
      if (confirmedAnswer.length < answer.length) {
        flag = 1;
        for (const a of confirmedAnswer) {
          if (answer.indexOf(a) < 0) {
            flag = 2;
            break;
          }
        }
      } else {
        flag = 2;
      }
    } else {
      const n = confirmedAnswer.length;
      for (let i = 0; i < n; i++) {
        if (confirmedAnswer[i] !== answer[i]) {
          flag = 2;
          break;
        }
      }
    }
    return flag;
  }

  renderFeedback = () => {
    let {currentChapter, currentQuiz, confirmedAnswer} = this.state;
    if (!confirmedAnswer) {
      return <div />;
    }
    const quizzes = questions[currentChapter].quizzes;
    const quiz = quizzes[currentQuiz];
    console.log(confirmedAnswer)
    const isMulti = quiz.answer.length !== 1;
    if (!isMulti) {
      confirmedAnswer = [confirmedAnswer];
    }
    confirmedAnswer = confirmedAnswer.sort();
    console.log("Answer:", quiz.answer)
    const answer = quiz.answer.sort();
    console.log(confirmedAnswer, answer)

    const flag = this.verifyAnswer();

    let feedbackDomList = [];
    for (const confirmedOption of confirmedAnswer) {
      const feedback = this.findFeedBack(confirmedOption, quiz)
      console.log(feedback)
      if (feedback) {
        feedbackDomList.push(
          <p key={feedback.key}>
            {feedback.text}
          </p>
        )
      }
    }
    let b;
    if (flag === 0) {
      b = "Great! :)"
    } else if (flag === 1) {
      b = "Incomplete answer! Seems there is something missing..."
    } else {
      b = "Inappropriate answer... :("
    }
    return (
      <div className={flag === 0 ? styles.correct : flag === 1 ? styles.incomplete : styles.wrong}>
        <div className={styles.feedback}>
          <p><b>{b}</b></p>
          {feedbackDomList}
        </div>
      </div>
    );
  }

  render() {
    const {currentChapter, currentAnswer, confirmedAnswer, allDone} = this.state;
    return (
      <div className={styles.wrap}>
        <header className={styles.header}>
          <h1>
            <Link to={"/"} className={styles.link}>Professional Practice</Link>
          </h1>
        </header>
        <div className={styles.steps}>
          {
            !allDone && <Steps current={currentChapter}>
              {
                this.getSteps().map(item => (
                  <Steps.Step key={item}/>
                ))
              }
            </Steps>
          }
        </div>
        <div className={styles.main}>
          {
            allDone ?
              <div className={styles.allDone}>
                <h1>Congratulations!</h1>
                <p>You have passed all the questions!<br/>We hope these questions did help you and can give you some inspirations on your future work.</p>
                <div className={styles.allDoneButtons}>
                  <Button
                    onClick={this.onTryAgainClick}
                    className={styles.allDoneButton}
                    type={"primary"}
                  >
                    Try again
                  </Button>
                </div>
              </div>:
              <div>
                {this.renderChapter()}
                <Divider />
                {this.renderQuiz()}
                <div className={styles.buttons}>
                  <Button
                    className={styles.button}
                    type="primary"
                    disabled={!currentAnswer}
                    onClick={this.onAnswerConfirm}
                  >
                    Confirm
                  </Button>
                  <Button
                    className={styles.button}
                    disabled={!confirmedAnswer || this.verifyAnswer() !== 0}
                    onClick={this.onNextQuiz}
                  >
                    Next
                  </Button>
                </div>
                {this.renderFeedback()}
              </div>
          }
        </div>
      </div>
    )
  }
}

export default withRouter(Quiz);

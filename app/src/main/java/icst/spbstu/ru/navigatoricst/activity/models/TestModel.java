package icst.spbstu.ru.navigatoricst.activity.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TestModel implements Parcelable {

    private String question;
    private ArrayList<String> answers;
    private ArrayList<ArrayList<Integer>> scores;

    public TestModel(String question, ArrayList<String> answers, ArrayList<ArrayList<Integer>> scores) {
        this.question = question;
        this.answers = answers;
        this.scores = scores;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public ArrayList<ArrayList<Integer>> getScores() {
        return scores;
    }

    public ArrayList<Integer> getScores(int position) {
        return scores.get(position);
    }

    public void setScores(ArrayList<ArrayList<Integer>> scores) {
        this.scores = scores;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeList(answers);
        dest.writeList(scores);
    }

    private TestModel(Parcel in) {
        question = in.readString();
        in.readList(answers, TestModel.class.getClassLoader());
        in.readList(scores, TestModel.class.getClassLoader());
    }

    public static Creator<TestModel> CREATOR = new Creator<TestModel>() {
        @Override
        public TestModel createFromParcel(Parcel source) {
            return new TestModel(source);
        }

        @Override
        public TestModel[] newArray(int size) {
            return new TestModel[size];
        }
    };
}

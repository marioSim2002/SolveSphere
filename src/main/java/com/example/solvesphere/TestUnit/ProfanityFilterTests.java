package com.example.solvesphere.TestUnit;

import com.example.solvesphere.ValidationsUnit.ProfanityFilter;
import org.testng.annotations.Test;

import static org.testng.Assert.assertThrows;

public class ProfanityFilterTests {

    //positive
    @Test
    public void testWordsHahingValid(){
        ProfanityFilter profanityFilter = new ProfanityFilter();
        String result = profanityFilter.filterText("crap"); /// provided a banned word
        assert result.equals("****");
    }
    //positive
    @Test
    public void testWordsHahingWithinString(){
        ProfanityFilter profanityFilter = new ProfanityFilter();
        String result = profanityFilter.filterText("xXcrapXx"); /// provided wrapped banned word
        System.out.println(result);
        assert result.equals("xX****Xx");
    }


    //negative TC
    @Test
    public void testFilterText_NullInput_ThrowsException() {
        String input = "";
        ProfanityFilter profanityFilter = new ProfanityFilter();
        String result = profanityFilter.filterText(input);
        assert result.equals(input);
    }
}

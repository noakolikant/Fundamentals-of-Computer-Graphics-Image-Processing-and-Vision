function [ H_ransac ] = RANSAC_Wrapper(matches, fittingfn, distfn, ...
    degenfn, s, t, feedback, maxDataTrials, maxTrials)
%RANSAC_Wrapper
    x1 = [matches(:, 1:2), ones(1, size(matches, 1))'];
    x2 = [matches(:, 3:4), ones(1, size(matches, 1))'];
    
    [x1, T1] = Normalize2dHomPoints(x1);
    [x2, T2] = Normalize2dHomPoints(x2);
%     fittingfn = @DLT;
%     distfn = @homdistfn;
%     degenfn = isdegenerate;
%     t = 0.6;
%     s = 4; 
%     feedback = 0;
%     maxDataTrials = 100;
%     maxTrials = 1000;
    [H, inliers] = ransac([x1; x2], fittingfn, distfn, degenfn, s, t, feedback, maxDataTrials, maxTrials);
    H_fit = fittingfn([x1(:,inliers), x2(:,inliers)]);
    
    % Denormalise
    H_ransac = T2\H_fit*T1; 

end